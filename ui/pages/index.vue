<script setup lang="ts">
import { PAGES } from "~/server/global";

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
    <li v-for="[href, name] in PAGES">
      <a :href="href">{{ name }}</a>
    </li>
  </ul>
</template>

<style lang="css" scoped>
pre {
  display: inline;
}
</style>
