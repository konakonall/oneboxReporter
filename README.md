# oneboxReporter

Library That parse SDK Unit Test Report and send results email to recipients.

## Entry Class

`com.qcloud.ut_result_sender.App`

## Usage

```
export ARTIFACTS=/var/lib/jenkins/workspace/OneBox/artifacts
export RECIPIENTS=...
export CCS=...

echo "onebox.console=http://ip:port

onebox.email.server=xx
onebox.email.name=xx
onebox.email.passed=xx
onebox.email.from=xx
onebox.email.recipients=$RECIPIENTS
onebox.email.cc=$CCS
" > onebox.properties

mvn clean compile assembly:single;
rm -fr dep
mkdir -p dep
cp target/*.jar dep/
java -Dfile.encoding=UTF-8 -cp "src/main/resources:dep/*" com.qcloud.ut_result_sender.App $ARTIFACTS $BUILD_NUMBER
```
