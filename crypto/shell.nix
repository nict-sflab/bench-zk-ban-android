let
  pkgs = import <nixpkgs> {
    config.android_sdk.accept_license = true;
  };

  androidComposition = pkgs.androidenv.composeAndroidPackages {
    platformVersions = [ "35" ];
    includeNDK = true;
  };
in
pkgs.mkShell rec {
  name = "go-android-bind-dev";

  packages = with pkgs; [
    go
    jdk
    android-tools
    androidComposition.androidsdk
  ];

  ANDROID_HOME = "${androidComposition.androidsdk}/libexec/android-sdk";
  ANDROID_SDK_ROOT = ANDROID_HOME;
  ANDROID_NDK_ROOT = "${ANDROID_HOME}/ndk-bundle";
  JAVA_HOME = pkgs.jdk.home;

  shellHook = ''
    export GOPATH="$HOME/go"
    export PATH="$GOPATH/bin:$PATH"

    export ALLOW_NINJA_ENV=true
    export USE_CCACHE=1
    export ANDROID_API=23
    export ZK_BAN_BENCH_PATH=.
    export ZK_BAN_AAR=zk-ban-bench.aar

    if ! command -v gomobile >/dev/null 2>&1; then
      go install golang.org/x/mobile/cmd/gomobile@latest
    fi

    gomobile init
    gomobile bind \
      -v \
      -target=android \
      -androidapi "$ANDROID_API" \
      -o "$ZK_BAN_AAR" \
      .
  '';
}
