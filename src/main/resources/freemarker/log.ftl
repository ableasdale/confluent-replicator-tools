<!doctype html>
<html lang="en">
    <#include "includes/header.ftl">
    <body>
        <#include "includes/modal_spinner.ftl">
        <div class="container">
            <#include "includes/navigation.ftl">
            <main>
                <#include "includes/toast.ftl">
                <div class="px-3 py-1 my-3">
                  <h1 class="display-6 fw-semibold">Configs: <small class="text-muted">${name}</small></h1>
                </div>
                <div class="px-4 py-1 my-3">
<textarea id="log" style="width:1100px; height:750px; font-family: Courier, monospace;" autocomplete="off" autocorrect="off" autocapitalize="off" spellcheck="false">
<#list file as lines>
${lines}
</#list>
</textarea>
                </div>
            </main>
            <#include "includes/footer.ftl">
        </div>
    </body>
</html>