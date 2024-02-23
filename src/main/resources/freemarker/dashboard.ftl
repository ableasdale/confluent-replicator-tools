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
                    <h1 class="display-6 fw-semibold">Upload Log: <small class="text-muted">Drag Connect Log file into the box below</small></h1>
                    <form enctype="multipart/form-data" action="/upload" method="post" class="dropzone" id="dropzone"></form>
                    <h2 class="display-6 fw-semibold">Current</h2>
                    <ul>
                    <#list logsizes as key, value>
                        <li>${key} (${value}) items</li>
                    </#list>
                    </ul>
                </div>
            </main>
            <#include "includes/footer.ftl">
        </div>
    </body>
</html>