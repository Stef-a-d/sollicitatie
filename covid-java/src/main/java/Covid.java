import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import org.apache.commons.cli.*;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.Map;

public class Covid {

    public static void main(String[] args) throws IOException, InterruptedException, ParseException {

        Options options = getOptions();

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args);

        CovidRecordFilter filter = new CovidRecordFilter(cmd);

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://epistat.sciensano.be/Data/COVID19BE_CASES_AGESEX.csv"))
                .build();

        HttpResponse<InputStream> response = client.send(request, HttpResponse.BodyHandlers.ofInputStream());
        Reader input = new InputStreamReader(response.body());

        Map<String, Integer> records = parseCsv(filter, input);
        records.forEach((k,v) -> System.out.printf("%s: %s, Cases: %s%n", filter.getAggregate(), k, v));
    }

    public static Map<String, Integer> parseCsv(CovidRecordFilter filter, Reader input) {
        CsvToBean<CovidRecord> reader = new CsvToBeanBuilder<CovidRecord>(input)
                .withType(CovidRecord.class)
                .build();
        Map<String, Integer> records = new HashMap<>();
        reader.stream().filter(filter::filter).forEach(record -> records.merge(record.getAggregateValue(filter.getAggregate()), record.getCases(), Integer::sum));
        return records;
    }

    public static Options getOptions() {
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
