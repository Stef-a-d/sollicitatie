using System;
using System.Collections.Generic;
using System.Diagnostics.Metrics;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace covid_net
{
    public class CovidRecord(DateOnly date, string province, string region, string ageGroup, string sex, int cases)
    {
        public DateOnly Date { get; } = date;
        public string Province { get; } = province;
        public string Region { get; } = region;
        public string AgeGroup { get; } = ageGroup;
        public string Sex { get; } = sex;
        public int Cases { get; } = cases;

        public string GetAggregation(AggregationType? aggregationType)
        {
            if (aggregationType == null)
            {
                return "All";
            }
            switch (aggregationType)
            {
                case AggregationType.Province: return Province;
                case AggregationType.Region: return Region;
                case AggregationType.Age: return AgeGroup;
                case AggregationType.Sex: return Sex;
                default: throw new NotSupportedException(string.Format("Aggregation Type {0} not regocnized", aggregationType));
            }
        }

        public bool Matches(Option o)
        {
            if (o.Before != null && Date >= o.Before)
            {
                return false;
            }
            if (o.After != null && Date <= o.After)
            {
                return false;
            }
            if (o.Province != null && !Province.Contains(o.Province))
            {
                return false;
            }
            if (o.Region != null && !Region.Contains(o.Region))
            {
                return false;
            }
            if (o.Sex != null && !Sex.Contains(o.Sex))
            {
                return false;
            }
            if(o.Age != null && !AgeGroup.Contains(o.Age))
            {
                return false;
            }

            return true;
        }
    }
}
