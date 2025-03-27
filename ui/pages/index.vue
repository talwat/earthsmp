<script setup lang="ts">
    const data = useAuth()
</script>

<template>
    <h1>EarthSMP Web Interface</h1>
    <h2>Session: {{ data }}</h2>
    <ul>
        <li><a href="/borders">Borders</a></li>
    </ul>
</template>