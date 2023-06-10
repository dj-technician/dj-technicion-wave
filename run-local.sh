#!/bin/sh

DEBUG=false
CLEAN=""
RUN_MODULE=""
RUN_DIRECTORY=""
MODE="local"
RUN_TYPE="spring" ## spring or npm

# parse arguments
while (("$#")); do
  if [ "-debug" = $1 ]; then
    DEBUG=true
    echo "DEBUG = ${DEBUG}"
  fi
  if [ "-clean" = $1 ]; then
    CLEAN=clean
    echo "CLEAN = true"
  fi
  if [ "-admin" = $1 ]; then
    RUN_MODULE=wave-game
  fi
  if [ "-api" = $1 ]; then
    RUN_MODULE=wave-api
  fi
  if [ "-local" = $1 ]; then
    MODE=local
  fi
  if [ "-dev" = $1 ]; then
    MODE=dev
  fi
  if [ "-admin-vue" = $1 ]; then
    RUN_TYPE="npm"
    RUN_DIRECTORY="./tahiti-admin/src/front"
  fi
  shift
done

# process by run_type
if [ ${RUN_TYPE} = "npm" ]; then
  cd ${RUN_DIRECTORY}
  npm install
  npm run serve
fi
if [ ${RUN_TYPE} = "spring" ]; then

  # process run options
  ARGS="--stacktrace --debug"
  if [ ${DEBUG} = true ]; then
    ARGS="${ARGS} --debug-jvm"
  fi
  if [ -z ${RUN_MODULE} ]; then
    echo 'you should choose module option (-api, -game)'
    exit 1
  fi

  # make full args
  # shellcheck disable=SC2089
  FULL_ARGS="./gradlew ${CLEAN} $RUN_MODULE:bootRun ${ARGS} --args='--spring.profiles.active=${MODE}'"

  echo RUN_MODULE : ${RUN_MODULE}
  echo ARGS : "${ARGS}"
  echo MODE : ${MODE}
  echo FULL_ARGS : "${FULL_ARGS}"

  # bootRun
  # shellcheck disable=SC2090
  ${FULL_ARGS}
  #./gradlew ${RUN_MODULE}:bootRun ${ARGS}
fi
