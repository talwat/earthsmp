<script setup lang="ts">
import { PAGES, PAGES_JOURNALIST } from "~/server/global";

const data = useAuth();

export interface User {
  name: string;
  role: string;
}

let user = data.data.value?.user! as User;
let pages = user.role == "admin" ? PAGES.reverse() : PAGES_JOURNALIST.reverse();
</script>

<template>
  <h1>
    Welcome,
    <pre>{{ data.data.value?.user?.name }}</pre>
    .
  </h1>
  <ul>
    <li v-for="[href, name] in pages">
      <a :href="href">{{ name }}</a>
    </li>
  </ul>
</template>

<style>
h1 {
  font-size: 4em;
}
</style>

<style lang="css" scoped>
pre {
  display: inline;
}

ul {
  display: flex;
  flex-direction: column;
  gap: 0.6em;
  align-items: center;
  padding: 0;
}

li {
  display: block;
  text-align: center;
  font-size: 3em;
  padding: 0.2em;
  width: 10em;
  max-width: 100%;
  box-sizing: border-box;
  background-color: darkslategrey;
}

a {
  color: white;
  text-decoration: none;
  display: block;
}
</style>
