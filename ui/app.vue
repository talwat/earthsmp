<script setup lang="ts">
import "@/global.css";
import { PAGES, PAGES_JOURNALIST } from "./server/global";
import type { User } from "./pages/index.vue";

let { user, loggedIn, openInPopup } = useUserSession();

let data = user.value as User | null;
let pages = data && data.role == "admin" ? PAGES : PAGES_JOURNALIST;
</script>

<template>
  <template v-if="loggedIn">
    <header>
      <nav class="top-bar">
        <h2><a href="/">Earth UI</a></h2>
        <ul class="nav-items">
          <li v-for="[href, name] in pages">
            <a :href="href">{{ name }}</a>
          </li>
        </ul>
      </nav>
    </header>
    <main style="margin-top: 5em">
      <NuxtPage />
    </main>
  </template>
  <main v-else>
    <h1>Not Logged In</h1>
    <div class="login-buttons">
      <button @click="openInPopup('/auth/github')">Login with GitHub</button>
    </div>
  </main>
</template>

<style lang="css" scoped>
.login-buttons {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
}

.login-buttons > button {
  background-color: black;
  color: white;
  font-size: 1.5rem;
  font-family: inherit;
}

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
  flex-grow: 1;
  display: flex;
  flex-direction: row-reverse;
  justify-content: flex-start;
  align-items: center;
  margin-right: 32px;
  padding-left: 0;
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

@media (max-width: 600px) {
  .nav-items {
    display: none;
  }

  h2 {
    flex-grow: 1;
    margin: 0;
    text-align: center;
    font-size: 2em;
  }
}
</style>
