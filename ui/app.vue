<script setup lang="ts">
import "@/global.css";
import { PAGES } from "./server/global";
</script>

<template>
  <header>
    <nav class="top-bar">
      <h2><a href="/">Earth UI</a></h2>
      <ul class="nav-items">
        <li><a href="/api/auth/signout">Sign Out</a></li>
        <li v-for="[href, name] in PAGES">
          <a :href="href">{{ name }}</a>
        </li>
      </ul>
    </nav>
  </header>
  <main>
    <NuxtPage />
  </main>
</template>

<style lang="css" scoped>
.top-bar {
  background-color: white;
  border-bottom: 1px solid black;
  position: fixed;
  top: 0;
  width: 100vw;
  height: 5em;
  margin: 0;
  padding: 0;
  box-shadow: 0 4px 4px 0 rgb(0, 0, 0, 20%);
  z-index: 2;
  display: flex;
  align-items: center;
  justify-content: space-between;
  overflow: hidden;
  align-content: stretch;
}

.nav-items {
  display: flex;
  flex-direction: row-reverse;
  align-items: center;
  margin-right: 32px;
  gap: 2em;
  width: 50vw;
}

.nav-items > li {
  display: inline;
}

.nav-items > li > a {
  font-size: 1.05em;
}

a {
  color: black;
  text-decoration: none;
}

h2 {
  margin: 0;
  margin-left: 32px;
  font-size: 1.7em;
}
</style>
