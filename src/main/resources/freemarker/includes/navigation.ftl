  <nav class="navbar navbar-expand-lg navbar-dark bg-dark">
  <!-- nav class="navbar navbar-expand-lg bg-light" -->
    <div class="container-fluid">
      <a class="navbar-brand" href="http://developer.confluent.io"><img src="../assets/logo-confluent-new.svg" alt="Confluent" width="200" height="30"></a>
      <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarSupportedContent" aria-controls="navbarSupportedContent" aria-expanded="false" aria-label="Toggle navigation">
        <span class="navbar-toggler-icon"></span>
      </button>
      <div class="collapse navbar-collapse" id="navbarSupportedContent">
        <ul class="navbar-nav me-auto mb-2 mb-lg-0">
          <li class="nav-item">
            <a class="nav-link active" aria-current="page" href="/">Dashboard</a>
          </li>
          <li class="nav-item dropdown">
            <a class="nav-link dropdown-toggle" href="#" role="button" data-bs-toggle="dropdown" aria-expanded="false">Log Messages</a>
            <ul class="dropdown-menu">
                <#list configs?keys as key>
                    <li><a class="dropdown-item" href="/logs/${key}">${key}</a></li>
                </#list>
            </ul>
          </li>
          <li class="nav-item dropdown">
            <a class="nav-link dropdown-toggle" href="#" role="button" data-bs-toggle="dropdown" aria-expanded="false">Configs</a>
            <ul class="dropdown-menu">
                <#list configs?keys as key>
                    <li><a class="dropdown-item" href="/configs/${key}">${key}</a></li>
                </#list>
            </ul>
          </li>
          <!-- li class="nav-item">
            <a class="nav-link disabled">Disabled</a>
          </li -->
        </ul>
        <!-- form class="d-flex" role="search">
          <input class="form-control me-2" type="search" placeholder="Search" aria-label="Search">
          <button class="btn btn-outline-success" type="submit">Search</button>
        </form -->
      </div>
    </div>
  </nav>

