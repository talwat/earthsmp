<script setup lang="ts">
import { PAGES, PAGES_JOURNALIST } from "~/server/global";

export interface User {
  name: string;
  role: string;
}

const { user, clear } = useUserSession();
const data = user.value as User | null;

let pages =
  data?.role == "admin" ? PAGES.reverse() : PAGES_JOURNALIST.reverse();

function logout() {
  if (confirm("Are you sure you'd like to log out?")) {
    clear();
  }
}
</script>

<template>
  <h1>
    Welcome,
    <pre>{{ data?.name }}</pre>
    .
  </h1>
  <ul>
    <li v-for="[href, name] in pages">
      <a :href="href">{{ name }}</a>
    </li>
    <button class="logout-btn" @click="logout">Log Out</button>
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
  font-size: 3rem;
  padding: 0.2em;
  width: 30rem;
  max-width: 100%;
  box-sizing: border-box;
  background-color: darkslategrey;
}

a {
  color: white;
  text-decoration: none;
  display: block;
}

.logout-btn {
  font-size: inherit;
  border: 4px solid lightcoral;
  font-size: 2rem;
}
</style>
