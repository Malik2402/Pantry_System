param(
    [Parameter(Mandatory=$true)][string]$Message,
    [Parameter(Mandatory=$true)][string[]]$Paths,
    [string[]]$Tasks = @('assembleDebug')
)
$ErrorActionPreference = 'Stop'
$gitCommand = (Get-Command git -ErrorAction SilentlyContinue).Source
if (-not $gitCommand) { $gitCommand = 'C:\Program Files\Git\cmd\git.exe' }
& .\gradlew.bat @Tasks --console=plain -q
if ($LASTEXITCODE -ne 0) { throw 'Build/test failed; checkpoint stopped.' }
& $gitCommand diff --check
if ($LASTEXITCODE -ne 0) { throw 'Whitespace review failed.' }
& $gitCommand add -- @Paths
if ($LASTEXITCODE -ne 0) { throw 'Staging failed.' }
$staged = & $gitCommand diff --cached --name-only
if ($staged -match '(^|/)(build|\.gradle|\.idea)/|local\.properties|\.(jks|keystore|pem|key|mp4|zip)$|(^|/)\.env') {
    throw 'Excluded file staged; review required.'
}
$diff = & $gitCommand diff --cached
if ($diff -match 'gh[pousr]_[A-Za-z0-9]{30,}|github_pat_[A-Za-z0-9_]{30,}|-----BEGIN (RSA |EC |OPENSSH )?PRIVATE KEY-----') {
    throw 'Potential credential staged; review required.'
}
& $gitCommand diff --cached --check
if ($LASTEXITCODE -ne 0) { throw 'Staged review failed.' }
& $gitCommand diff --cached --stat
& $gitCommand commit -m $Message
if ($LASTEXITCODE -ne 0) { throw 'Commit failed.' }
& $gitCommand push origin main
if ($LASTEXITCODE -ne 0) { throw 'Push failed.' }
$localCommit = & $gitCommand rev-parse HEAD
$remoteLine = & $gitCommand ls-remote origin refs/heads/main
if ($LASTEXITCODE -ne 0 -or -not $remoteLine.StartsWith($localCommit)) {
    throw 'Remote verification failed.'
}
Write-Output "VERIFIED $localCommit $Message"
