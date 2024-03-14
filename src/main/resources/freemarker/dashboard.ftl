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
                    <h2>Upload Log: <small class="text-muted">Drag Connect Log file into the box below</small></h2>
                    <form enctype="multipart/form-data" action="/upload" method="post" class="dropzone" id="dropzone"></form>
                    <hr>
                </div>
                <div class="px-3 py-1 my-3">
                    <h3>Current File: <small class="text-muted">${filename}</small></h2>
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
                    <h3>Support Knowledgebase Articles</h3>
                    <ul>
                        <li><a href="https://support.confluent.io/hc/en-us/articles/5582610489620-How-to-investigate-out-of-range-partition-offset-warnings-when-using-replicator-executable">How to investigate out-of-range partition offset warnings when using replicator executable</a></li>
                        <li><a href="https://support.confluent.io/hc/en-us/articles/360041690872-How-to-reset-offsets-for-Replicator-to-re-read-skip-messages-for-a-topic">How to reset offsets for Replicator to re-read/skip messages for a topic</a></li>
                        <li><a href="https://support.confluent.io/hc/en-us/articles/360052618911-What-s-causing-UNKNOWN-TOPIC-OR-PARTITION-in-Connect-Worker-Logs-after-a-source-connector-deletion">What's causing UNKNOWN_TOPIC_OR_PARTITION in Connect Worker Logs after a source connector deletion</a></li>
                        <li><a href="https://support.confluent.io/hc/en-us/articles/360053074272-What-s-causing-Kafka-client-request-disconnect-or-timeout-errors">What's causing Kafka client request disconnect or timeout errors</a></li>
                        <li><a href="https://support.confluent.io/hc/en-us/articles/360051921532-Why-there-is-a-WakeUpException-observed-within-confluent-replicator">Why there is a WakeUpException observed within confluent replicator</a></li>
                        <li><a href="https://support.confluent.io/hc/en-us/articles/360051860452-Confluent-Replicator-may-result-in-lot-of-duplicate-messages-if-offset-start-consumer">Confluent Replicator may result in lot of duplicate messages if offset.start=consumer</a></li>
                        <li><a href="https://support.confluent.io/hc/en-us/articles/360056467652-What-s-causing-connector-and-tasks-fail-with-ConnectException-Task-already-exists-in-this-worker">What's causing connector and tasks fail with "ConnectException: Task already exists in this worker"</a></li>
                        <li><a href="https://support.confluent.io/hc/en-us/articles/360052121292-What-can-cause-connector-tasks-being-in-an-UNASSIGNED-state">What can cause connector tasks being in an UNASSIGNED state</a></li>
                    </ul>
                </div>
            </main>
            <#include "includes/footer.ftl">
        </div>
    </body>
</html>