package crypto

import (
	"fmt"
	"runtime"

	"github.com/akakou/zk-ban/bench/latency"
	"github.com/akakou/zk-ban/bench/latency/utils/usefulbench"
	"github.com/akakou/zk-ban/circuit"
	"github.com/akakou/zk-ban/test"
	_ "golang.org/x/mobile/bind"
)

func Benchmark(path string) string {
	res := benchmark(30000, 30, 5, "mobile-baseline-", path)

	latency.OnlyUpdate = true
	res += benchmark(300000, 30, 5, "mobile-large-nyms-", path)
	res += benchmark(30000, 720, 5, "mobile-large-periods-", path)
	res += benchmark(30000, 30, 100, "mobile-large-max-session-", path)

	fmt.Printf("\nresult: %v", res)

	return res
}

func benchmark(baseSubSevocation, basePeriod, baseMaxSession int, name, path string) string {
	latency.SkipVerify = true
	latency.OnlyUniform = true
	fmt.Printf("NumCPU=%d GOMAXPROCS=%d\n", runtime.NumCPU(), runtime.GOMAXPROCS(0))

	latency.Name = name

	loop := 1
	b := usefulbench.New(loop)
	test.TestKeyPath = path

	latency.BaseSubRevocation = baseSubSevocation
	latency.BasePeriods = basePeriod
	circuit.MaxSession = baseMaxSession

	latency.BenchmarkBaseline(b)

	res := b.ResultJson() + "\n"
	fmt.Printf("%v", res)

	return res
}
