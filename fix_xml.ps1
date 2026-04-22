# fix_xml.ps1
$files = Get-ChildItem -Path "app\src\main\res" -Recurse -Include "*.xml"
foreach ($file in $files) {
    $content = Get-Content $file.FullName -Raw
    # 修复 & 符号
    $newContent = $content -replace '&(?!amp;)', '&amp;'
    # 确保文件以 <?xml 开头（如果第一行不是）
    if ($newContent -notmatch '^\s*<\?xml') {
        $newContent = '<?xml version="1.0" encoding="utf-8"?>' + "`n" + $newContent
    }
    # 保存为 UTF-8 无 BOM
    $utf8NoBom = New-Object System.Text.UTF8Encoding $false
    [System.IO.File]::WriteAllText($file.FullName, $newContent, $utf8NoBom)
}
Write-Host "所有 XML 文件已修复！"