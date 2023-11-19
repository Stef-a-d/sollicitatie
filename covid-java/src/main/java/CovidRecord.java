import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvDate;

import java.time.LocalDate;
import java.util.function.Function;
import java.util.function.Supplier;

public class CovidRecord {
    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getAgeGroup() {
        return ageGroup;
    }

    public void setAgeGroup(String ageGroup) {
        this.ageGroup = ageGroup;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public int getCases() {
        return cases;
    }

    public void setCases(int cases) {
        this.cases = cases;
    }

    @CsvBindByName(column = "Date")
    @CsvDate(value = "yyyy-MM-dd")
    private LocalDate date;
    @CsvBindByName(column = "Province")
    private String province;
    @CsvBindByName(column = "Region")
    private String region;
    @CsvBindByName(column = "AgeGroup")
    private String ageGroup;
    @CsvBindByName(column = "Sex")
    private String sex;
    @CsvBindByName(column = "Cases")
    private int cases;

    @Override
    public String toString() {
        return String.join(",", new String[]{date.toString(), province, region, ageGroup, sex, String.valueOf(cases)});
    }

    public String getAggregateValue(Aggregate aggregate){
        switch (aggregate){
            case PROVINCE -> {
                return this.getProvince();
            }
            case REGION -> {
                return this.getRegion();
            }
            case AGE -> {
                return this.getAgeGroup();
            }
            case SEX -> {
                return this.getSex();
            }
            default -> {
                return "All";
            }
        }
    }
}
