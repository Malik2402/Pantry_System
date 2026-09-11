param([Parameter(Mandatory=$true)][string]$Serial)
$ErrorActionPreference='Stop'
if(-not $Serial.StartsWith('emulator-')) { throw 'Use a dedicated emulator; these tests reset the app pantry.' }
$adb=Join-Path $env:ANDROID_HOME 'platform-tools\adb.exe'
& .\gradlew.bat assembleDebug assembleDebugAndroidTest testDebugUnitTest lintDebug --console=plain -q
if($LASTEXITCODE -ne 0){throw 'Build or checks failed.'}
& $adb -s $Serial install -r app/build/outputs/apk/debug/app-debug.apk
if($LASTEXITCODE -ne 0){throw 'App install failed.'}
& $adb -s $Serial install -r app/build/outputs/apk/androidTest/debug/app-debug-androidTest.apk
if($LASTEXITCODE -ne 0){throw 'Test install failed.'}
New-Item -ItemType Directory -Force -Path docs/evidence | Out-Null
$result=& $adb -s $Serial shell am instrument -w -e class 'za.co.smartpantry.AppFlowTest,za.co.smartpantry.DatabaseTest,za.co.smartpantry.PreferencesTest' za.co.smartpantry.test/androidx.test.runner.AndroidJUnitRunner 2>&1
[IO.File]::WriteAllText((Join-Path $PWD 'docs/evidence/device-tests.txt'),($result -join "`n").TrimEnd()+"`n")
if($LASTEXITCODE -ne 0 -or ($result -join "`n") -notmatch 'OK \(9 tests\)'){throw 'Device tests failed; inspect docs/evidence/device-tests.txt.'}
& $adb -s $Serial shell am force-stop za.co.smartpantry
if($LASTEXITCODE -ne 0){throw 'Force-stop failed.'}
$reopen=& $adb -s $Serial shell am instrument -w -e class 'za.co.smartpantry.AppFlowTest#b_persistenceAfterReopen' za.co.smartpantry.test/androidx.test.runner.AndroidJUnitRunner 2>&1
$reopenLog=@('App was force-stopped before this separate instrumentation run.',"Verified at $(Get-Date -Format o)") + $reopen
[IO.File]::WriteAllText((Join-Path $PWD 'docs/evidence/process-reopen.txt'),($reopenLog -join "`n").TrimEnd()+"`n")
if($LASTEXITCODE -ne 0 -or ($reopen -join "`n") -notmatch 'OK \(1 test\)'){throw 'Process reopening test failed.'}
& $adb -s $Serial pull /sdcard/Android/data/za.co.smartpantry/files/evidence/. docs/evidence
if($LASTEXITCODE -ne 0){throw 'Screenshot copy failed.'}
$images=Get-ChildItem -LiteralPath docs/evidence -Filter '*.png'
if($images.Count -ne 18){throw 'Expected eighteen screenshots.'}
Get-ChildItem -LiteralPath app/build/test-results/testDebugUnitTest -Filter '*.xml' | Copy-Item -Destination docs/evidence
Copy-Item -LiteralPath app/build/reports/lint-results-debug.txt -Destination docs/evidence/lint-results.txt
$homeResult=& $adb -s $Serial shell am instrument -w -e class 'za.co.smartpantry.HomeScreenTest' za.co.smartpantry.test/androidx.test.runner.AndroidJUnitRunner 2>&1
[IO.File]::WriteAllText((Join-Path $PWD 'docs/evidence/home-tests.txt'),($homeResult -join "`n").TrimEnd()+"`n")
if($LASTEXITCODE -ne 0 -or ($homeResult -join "`n") -notmatch 'OK \(2 tests\)'){throw 'Home screen tests failed.'}
New-Item -ItemType Directory -Force -Path docs/evidence/home-review | Out-Null
& $adb -s $Serial pull /sdcard/Android/data/za.co.smartpantry/files/home-review/. docs/evidence/home-review
if($LASTEXITCODE -ne 0){throw 'Home screenshot copy failed.'}
Write-Output 'Verified nine existing device tests, two home tests, a separate process-reopen test and screenshots.'
