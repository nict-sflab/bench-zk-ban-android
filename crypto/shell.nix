let pkgs = import <nixpkgs> {};

in pkgs.mkShell rec {
  name = "zk-ban-bench";

  buildInputs = with pkgs; [
    # android-studio
    android-tools
    go gomobile
    jdk gcc
  ];

  shellHook = ''
    export ALLOW_NINJA_ENV=true
    export GOPATH=/home/akakou/go
    export USE_CCACHE=1
    export ANDROID_JAVA_HOME=${pkgs.jdk.home}sdkmanager install avd
    export LD_LIBRARY_PATH=/usr/lib:/usr/lib32

    ZK_BAN_BENCH_PATH=.
    ZK_BAN_BENCH_AAR=zk-ban-bench.aar
    ZK_BAN_BENCH_PACKAGE=github.com/akakou/benchzkban/crypto
    ANDROID_API=23 

    export GOPATH=$HOME/go
    gomobile clean
    gomobile init

    cd $ZK_BAN_BENCH_PATH
    gomobile bind -o $ZK_BAN_BENCH_AAR -target=android -androidapi $ANDROID_API .
  '';
}
