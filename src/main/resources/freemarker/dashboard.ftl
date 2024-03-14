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
                    <hr>
                </div>
                <div class="px-3 py-1 my-3">
                    <h2 class="display-7 fw-semibold">Current File: <small class="text-muted">${filename}</small></h2>
                    <div class="card" style="width: 35rem;">
                      <div class="card-header">
                        ${filename}
                      </div>
                      <ul class="list-group list-group-flush">
                          <#list logsizes as key, value>
                            <li class="list-group-item"><a href="/logs/${key}">${key}</a> (${value}) items</li>
                          </#list>
                      </ul>
                    </div>
                </div>
                <div class="px-3 py-1 my-3">
                    <ul>
                        <h3>Support Knowledgebase Articles</h3>
                        <li><a href="https://support.confluent.io/hc/en-us/articles/5582610489620-How-to-investigate-out-of-range-partition-offset-warnings-when-using-replicator-executable">How to investigate out-of-range partition offset warnings when using replicator executable</a>
                    </ul>
                </div>
            </main>
            <#include "includes/footer.ftl">
        </div>
    </body>
</html>