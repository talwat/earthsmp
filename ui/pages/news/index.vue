<script setup lang="ts">
export interface Article {
  date: string;
  headline: string;
}

let list: Article[] = await $fetch("/api/news/list");
</script>

<template>
  <h1>News</h1>
  <ol>
    <li>
      <button class="large-square-btn"><Plus></Plus></button>
    </li>
    <li v-for="article in list">
      <a :href="`/news/edit/${article.date}`">
        <span class="date">{{ new Date(article.date).toDateString() }} - </span>
        <span>{{ article.headline }}</span>
      </a>
    </li>
  </ol>
</template>

<style lang="css" scoped>
span {
  font-size: 1.5em;
}

ol {
  padding: 0;
}

li {
  display: block;
}

.date {
  font-family: monospace;
}

.large-square-btn {
  width: 32px;
  height: 32px;
  min-width: 32px;
  min-height: 32px;
}

.large-square-btn > svg {
  width: 100%;
  height: 100%;
}
</style>
