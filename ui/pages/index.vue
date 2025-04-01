<script setup lang="ts">
const data = useAuth();
</script>

<template>
  <h1>
    Welcome,
    <pre>{{ data.data.value?.user?.name }}</pre>
    .
  </h1>
  <div>
    <a href="/api/auth/signout">Sign Out</a>
  </div>
  <ul>
    <li><a href="/borders">Borders</a></li>
    <li><a href="/nations">Nations</a></li>
  </ul>
</template>

<style lang="css" scoped>
pre {
  display: inline;
}
</style>
