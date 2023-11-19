import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import org.apache.commons.cli.*;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class Covid {

    public static void main(String[] args) throws IOException, InterruptedException, ParseException {

        Options options = getOptions();

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args);

        CovidRecordFilter filter = new CovidRecordFilter(cmd);

        Aggregate aggregate = Aggregate.ALL;
        if (cmd.hasOption("aggregate")) {
            String agg = cmd.getOptionValue("aggregate");
            aggregate = Aggregate.valueOf(agg.toUpperCase(Locale.ROOT));
        }

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://epistat.sciensano.be/Data/COVID19BE_CASES_AGESEX.csv"))
                .build();

        HttpResponse<InputStream> response = client.send(request, HttpResponse.BodyHandlers.ofInputStream());
        CsvToBean<CovidRecord> reader = new CsvToBeanBuilder<CovidRecord>(new InputStreamReader(response.body()))
                .withType(CovidRecord.class)
                .build();
        Map<String, Integer> records = new HashMap<>();
        Aggregate finalAggregate = aggregate;
        reader.stream().filter(filter::filter).forEach(record -> records.merge(record.getAggregateValue(finalAggregate), record.getCases(), Integer::sum));
        records.forEach((k,v) -> System.out.printf("%s: %s, Cases: %s%n", finalAggregate, k, v));
    }

    private static Options getOptions() {
        Options options = new Options();
        options.addOption(Option.builder().longOpt("before").hasArg().build());
        options.addOption(Option.builder().longOpt("after").hasArg().build());
        options.addOption(Option.builder().longOpt("aggregate").hasArg().build());
        options.addOption(Option.builder().longOpt("province").hasArg().build());
        options.addOption(Option.builder().longOpt("region").hasArg().build());
        options.addOption(Option.builder().longOpt("age").hasArg().build());
        options.addOption(Option.builder().longOpt("sex").hasArg().build());
        return options;
    }
}
