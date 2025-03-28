<script setup lang="ts">
const data = useAuth();
</script>

<template>
  <h1>EarthSMP Web Interface</h1>
  <h2>Welcome, {{ data.data.value?.user?.name }}</h2>
  <div>
    <a href="/api/auth/signout">Sign Out</a>
  </div>
  <ul>
    <li><a href="/borders">Borders</a></li>
  </ul>
</template>
