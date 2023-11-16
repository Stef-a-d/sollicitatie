using CommandLine;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace covid_net
{
    internal class Option
    {
        [Option("before")]
        public DateOnly? Before { get; set; }
        [Option("after")]
        public DateOnly? After { get; set; }
        [Option("aggregation")]
        public AggregationType? Aggregation { get; set; }
        [Option("province")]
        public string? Province { get; set; }

        [Option("region")]
        public string? Region { get; set; }
        [Option("age")]
        public string? Age { get; set; }
        [Option("sex")]
        public string? Sex { get; set; }


    }

    enum AggregationType
    {
        Province,
        Region,
        Age,
        Sex
    }
}
