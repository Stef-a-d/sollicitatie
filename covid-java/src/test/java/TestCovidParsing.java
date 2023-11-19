import org.apache.commons.cli.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.Map;

public class TestCovidParsing {
    private Reader input;

    private final int first = 52;
    private final int second = 25;

    @BeforeEach
    public void setup(){
        String csv = """
                DATE,"PROVINCE","REGION","AGEGROUP","SEX","CASES"
                2021-03-19,"Antwerpen","Flanders","50-59","M",52
                2021-04-19,"BrabantWallon","Wallonia","0-9","F",25
                """;
        input = new StringReader(csv);
    }

    @AfterEach
    public void teardown() throws IOException {
        input.close();
    }

    @Test
    public void testProvince() throws ParseException {
        CommandLine cmd = getCommandLineHelper(new String[]{"--province", "Antwerpen"});
        CovidRecordFilter filter = new CovidRecordFilter(cmd);
        Map<String, Integer> cases = Covid.parseCsv(filter, input);
        Assertions.assertEquals(cases.size(), 1);
        Assertions.assertEquals(cases.get("All"), first);
    }

    @Test
    public void testRegion() throws ParseException {
        CommandLine cmd = getCommandLineHelper(new String[]{"--region", "Wallonia"});
        CovidRecordFilter filter = new CovidRecordFilter(cmd);
        Map<String, Integer> cases = Covid.parseCsv(filter, input);
        Assertions.assertEquals(cases.size(), 1);
        Assertions.assertEquals(cases.get("All"), second);
    }

    @Test
    public void testAge() throws ParseException {
        CommandLine cmd = getCommandLineHelper(new String[]{"--age", "50-59"});
        CovidRecordFilter filter = new CovidRecordFilter(cmd);
        Map<String, Integer> cases = Covid.parseCsv(filter, input);
        Assertions.assertEquals(cases.size(), 1);
        Assertions.assertEquals(cases.get("All"), first);
    }

    @Test
    public void testSex() throws ParseException {
        CommandLine cmd = getCommandLineHelper(new String[]{"--sex", "F"});
        CovidRecordFilter filter = new CovidRecordFilter(cmd);
        Map<String, Integer> cases = Covid.parseCsv(filter, input);
        Assertions.assertEquals(cases.size(), 1);
        Assertions.assertEquals(cases.get("All"), second);
    }
    @Test
    public void testAll() throws ParseException {
        CommandLine cmd = getCommandLineHelper(new String[]{});
        CovidRecordFilter filter = new CovidRecordFilter(cmd);
        Map<String, Integer> cases = Covid.parseCsv(filter, input);
        Assertions.assertEquals(cases.size(), 1);
        Assertions.assertEquals(cases.get("All"), first + second);
    }
    @Test
    public void testDateBefore() throws ParseException {
        CommandLine cmd = getCommandLineHelper(new String[]{"--before", "2021-03-20"});
        CovidRecordFilter filter = new CovidRecordFilter(cmd);
        Map<String, Integer> cases = Covid.parseCsv(filter, input);
        Assertions.assertEquals(cases.size(), 1);
        Assertions.assertEquals(cases.get("All"), first);
    }
    @Test
    public void testDateAfter() throws ParseException {
        CommandLine cmd = getCommandLineHelper(new String[]{"--after", "2021-03-20"});
        CovidRecordFilter filter = new CovidRecordFilter(cmd);
        Map<String, Integer> cases = Covid.parseCsv(filter, input);
        Assertions.assertEquals(cases.size(), 1);
        Assertions.assertEquals(cases.get("All"), second);
    }

    @Test
    public void testProvinceAggregate() throws ParseException {
        CommandLine cmd = getCommandLineHelper(new String[]{"--aggregate", "Province"});
        CovidRecordFilter filter = new CovidRecordFilter(cmd);
        Map<String, Integer> cases = Covid.parseCsv(filter, input);
        Assertions.assertEquals(cases.size(), 2);
        Assertions.assertEquals(cases.values().stream().mapToInt(Integer::intValue).sum(), first + second);
    }
    @Test
    public void testRegionAggregate() throws ParseException {
        CommandLine cmd = getCommandLineHelper(new String[]{"--aggregate", "Region"});
        CovidRecordFilter filter = new CovidRecordFilter(cmd);
        Map<String, Integer> cases = Covid.parseCsv(filter, input);
        Assertions.assertEquals(cases.size(), 2);
        Assertions.assertEquals(cases.values().stream().mapToInt(Integer::intValue).sum(), first + second);
    }
    @Test
    public void testAgeAggregate() throws ParseException {
        CommandLine cmd = getCommandLineHelper(new String[]{"--aggregate", "Age"});
        CovidRecordFilter filter = new CovidRecordFilter(cmd);
        Map<String, Integer> cases = Covid.parseCsv(filter, input);
        Assertions.assertEquals(cases.size(), 2);
        Assertions.assertEquals(cases.values().stream().mapToInt(Integer::intValue).sum(), first + second);
    }
    @Test
    public void testSexAggregate() throws ParseException {
        CommandLine cmd = getCommandLineHelper(new String[]{"--aggregate", "Sex"});
        CovidRecordFilter filter = new CovidRecordFilter(cmd);
        Map<String, Integer> cases = Covid.parseCsv(filter, input);
        Assertions.assertEquals(cases.size(), 2);
        Assertions.assertEquals(cases.values().stream().mapToInt(Integer::intValue).sum(), first + second);
    }

    public CommandLine getCommandLineHelper(String[] args) throws ParseException {
        Options options = Covid.getOptions();

        CommandLineParser parser = new DefaultParser();
        return parser.parse(options, args);
    }
}
