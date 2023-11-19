using covid_net;
using Microsoft.VisualStudio.TestPlatform.TestHost;
using System.Diagnostics.Metrics;
using System.IO;

namespace TestProject1
{
    [TestClass]
    public class UnitTest1
    {
        private readonly string csv = """
            DATE,"PROVINCE","REGION","AGEGROUP","SEX","CASES"
            2021-03-19,"Antwerpen","Flanders","50-59","M",52
            2021-04-19,"BrabantWallon","Wallonia","0-9","F",25
            """;

        private Stream csvStream;

        private readonly int first = 52;
        private readonly int second = 25;

        [TestMethod]
        public void TestProvince()
        {
            Option option = new Option();
            option.Province = "Antwerpen";
            Dictionary<String, int> antwerpen = covid_net.Program.CollectCases(option, csvStream);
            Assert.AreEqual(1, antwerpen.Count);
            Assert.AreEqual(first, antwerpen["All"]);
        }

        [TestMethod]
        public void TestRegion()
        {
            Option option = new Option();
            option.Region = "Wallonia";
            Dictionary<String, int> region = covid_net.Program.CollectCases(option, csvStream);
            Assert.AreEqual(1, region.Count);
            Assert.AreEqual(second, region["All"]);
        }

        [TestMethod]
        public void TestAge()
        {
            Option option = new Option();
            option.Age = "50-59";
            Dictionary<String, int> age = covid_net.Program.CollectCases(option, csvStream);
            Assert.AreEqual(1, age.Count);
            Assert.AreEqual(first, age["All"]);
        }

        [TestMethod]
        public void TestSex()
        {
            Option option = new Option();
            option.Sex = "F";
            Dictionary<String, int> sex = covid_net.Program.CollectCases(option, csvStream);
            Assert.AreEqual(1, sex.Count);
            Assert.AreEqual(second, sex["All"]);
        }

        [TestMethod]
        public void TestProvinceAggregation()
        {
            Option option = new Option();
            option.Aggregation = AggregationType.Province;
            Dictionary<string, int> aggregation = covid_net.Program.CollectCases(option, csvStream);
            Assert.AreEqual(2, aggregation.Count);
            Assert.AreEqual(first + second, aggregation.Values.Sum());
        }

        [TestMethod]
        public void TestRegionAggregation()
        {
            Option option = new Option();
            option.Aggregation = AggregationType.Region;
            Dictionary<string, int> aggregation = covid_net.Program.CollectCases(option, csvStream);
            Assert.AreEqual(2, aggregation.Count);
            Assert.AreEqual(first + second, aggregation.Values.Sum());
        }

        [TestMethod]
        public void TestAgeAggregation()
        {
            Option option = new Option();
            option.Aggregation = AggregationType.Age;
            Dictionary<string, int> aggregation = covid_net.Program.CollectCases(option, csvStream);
            Assert.AreEqual(2, aggregation.Count);
            Assert.AreEqual(first + second, aggregation.Values.Sum());
        }

        [TestMethod]
        public void TestSexAggregation()
        {
            Option option = new Option();
            option.Aggregation = AggregationType.Sex;
            Dictionary<string, int> aggregation = covid_net.Program.CollectCases(option, csvStream);
            Assert.AreEqual(2, aggregation.Count);
            Assert.AreEqual(first + second, aggregation.Values.Sum());
        }

        [TestMethod]
        public void TestDateBefore()
        {
            Option option = new Option();
            option.Before = new DateOnly(2021, 03, 20);
            Dictionary<String, int> before = covid_net.Program.CollectCases(option, csvStream);
            Assert.AreEqual(1, before.Count);
            Assert.AreEqual(first, before["All"]);
        }

        [TestMethod]
        public void TestDateAfter()
        {
            Option option = new Option();
            option.After = new DateOnly(2021, 03, 20);
            Dictionary<String, int> after = covid_net.Program.CollectCases(option, csvStream);
            Assert.AreEqual(1, after.Count);
            Assert.AreEqual(second, after["All"]);
        }

        [TestMethod]
        public void TestCount()
        {
            Option option = new Option();
            Dictionary<String, int> count = covid_net.Program.CollectCases(option, csvStream);
            Assert.AreEqual(1, count.Count);
            Assert.AreEqual(first + second, count["All"]);
        }

        [TestInitialize]
        public void Setup()
        {
            csvStream = new MemoryStream();
            var writer = new StreamWriter(csvStream);
            writer.Write(csv);
            writer.Flush();
            csvStream.Position = 0;
        }
    }
}