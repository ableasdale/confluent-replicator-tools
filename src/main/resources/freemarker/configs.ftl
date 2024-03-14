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
                  <h2>Configs: <small class="text-muted">${name}</small></h2>
                </div>
                <div class="px-4 py-1 my-3">
                <textarea id="textarea">
<#list file as user>
${user}
</#list>
                </textarea>
                </div>
            </main>
            <#include "includes/footer.ftl">
        </div>
    </body>
</html>