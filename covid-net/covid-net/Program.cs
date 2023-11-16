// See https://aka.ms/new-console-template for more information
using CommandLine;
using covid_net;
using CsvHelper;
using CsvHelper.Configuration;
using System.Globalization;


await CommandLine.Parser.Default.ParseArguments<Option>(args).WithParsedAsync(async o =>
{
    var client = new HttpClient();
    var message = new HttpRequestMessage(HttpMethod.Get, "https://epistat.sciensano.be/Data/COVID19BE_CASES_AGESEX.csv");
    var response = await client.SendAsync(message, HttpCompletionOption.ResponseHeadersRead);
    var stream = await response.Content.ReadAsStreamAsync();
    Dictionary<string, int> cases = new Dictionary<string, int>();

    using (var reader = new StreamReader(stream))
    {
        var config = new CsvConfiguration(CultureInfo.InvariantCulture)
        {
            PrepareHeaderForMatch = args => args.Header.ToUpper(),
        };
        using (var csv = new CsvReader(reader, config))
        {
            while (csv.Read())
            {
                var record = csv.GetRecord<CovidRecord>();
                if(record == null) { continue; }
                if (!record.Matches(o))
                {
                    continue;
                }

                string key = record.GetAggregation(o.Aggregation);
                _ = cases.TryAdd(key, 0);
                cases[key] += record.Cases;
            }
        }
    }

    foreach (KeyValuePair<string, int> kvp in cases)
    {
        Console.WriteLine("{0} : {1}, Cases = {2}", o.Aggregation?.ToString() ?? "Cases", kvp.Key, kvp.Value);
    }
});
