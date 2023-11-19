import org.apache.commons.cli.CommandLine;

import java.time.LocalDate;
import java.util.Locale;
import java.util.function.Predicate;

public class CovidRecordFilter {

    private Predicate<CovidRecord> filter;
    private Aggregate aggregate;

    public CovidRecordFilter(CommandLine cmd){
        filter = (record -> true);

        if (cmd.hasOption("before")) {
            LocalDate before = LocalDate.parse(cmd.getOptionValue("before"));
            filter = filter.and(record -> record.getDate().isBefore(before));
        }
        if (cmd.hasOption("after")) {
            LocalDate after = LocalDate.parse(cmd.getOptionValue("after"));
            filter = filter.and(record -> record.getDate().isAfter(after));
        }
        if (cmd.hasOption("province")) {
            String province = cmd.getOptionValue("province");
            filter = filter.and(record -> record.getProvince().contains(province));
        }
        if (cmd.hasOption("region")) {
            String region = cmd.getOptionValue("region");
            filter = filter.and(record -> record.getRegion().contains(region));
        }
        if (cmd.hasOption("age")) {
            String age = cmd.getOptionValue("age");
            filter = filter.and(record -> record.getAgeGroup().contains(age));
        }
        if (cmd.hasOption("sex")) {
            String sex = cmd.getOptionValue("sex");
            filter = filter.and(record -> record.getSex().contains(sex));
        }
        aggregate = Aggregate.ALL;
        if (cmd.hasOption("aggregate")) {
            String agg = cmd.getOptionValue("aggregate");
            aggregate = Aggregate.valueOf(agg.toUpperCase(Locale.ROOT));
        }
    }

    public boolean filter(CovidRecord record){
        return filter.test(record);
    }


    public Aggregate getAggregate() {
        return aggregate;
    }
}
