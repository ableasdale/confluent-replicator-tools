<#assign kc = kafka_clusters?eval_json>
<table class="table table-bordered table-striped">
  <thead class="table-dark">
    <tr>
      <th class="text-center" scope="col">Availability</th>
      <th scope="col">ID</th>
      <th scope="col">Name</th>
      <th class="text-center" scope="col">Provider</th>
      <th class="text-center" scope="col">Region</th>
      <th class="text-center" scope="col">Status</th>
      <th class="text-center" scope="col">Type</th>
    </tr>
  </thead>
  <tbody>
  <#list kc as cluster>
    <tr>
        <td class="text-center">${cluster.availability}</td>
        <td>${cluster.id}</td>
        <td>${cluster.name}</td>
        <td class="text-center">${cluster.provider}</td>
        <td class="text-center">${cluster.region}</td>
        <td class="text-center<#if cluster.status?contains("UP")> table-success<#else> table-danger</#if>">${cluster.status}</td>
        <td class="text-center">${cluster.type}</td>
    </tr>
  </#list>
  </tbody>
</table>
