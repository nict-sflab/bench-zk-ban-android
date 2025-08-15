package crypto

import (
	"fmt"

	"github.com/akakou/zk-ban/bench/latency"
	"github.com/akakou/zk-ban/bench/latency/utils/usefulbench"
	"github.com/akakou/zk-ban/test"
	_ "golang.org/x/mobile/bind"
)

func Benchmark() string {
	loop := 20

	test.SkipVerify = true

	b := usefulbench.New(loop)
	latency.BenchmarkBaseline(b)
	res := b.ResultJson()
	fmt.Printf("%v\n", res)

	return res
}
