<script setup lang="ts">
export interface Article {
  date: string;
  headline: string;
}

let add: Ref<Article, Article> = ref({} as Article);

// @ts-ignore
let list: Ref<Article[], Article[]> = ref(await $fetch("/api/news/list"));

async function addArticle() {
  if (!add.value.headline) {
    alert("You need a headline!");
    return;
  }

  if (!add.value.date) {
    alert("You need a date!");
    return;
  }

  await $fetch(`/api/news/new/${add.value.date}`, {
    body: {
      headline: add.value.headline,
    },
    method: "POST",
    headers: {
      "Content-Type": "application/json",
    },
  });

  list.value = await $fetch("/api/news/list");
}

async function removeArticle(date: string) {
  if (!confirm(`Are you sure you'd like to delete the article for ${date}?`)) {
    return;
  }

  await $fetch(`/api/news/delete/${date}`, {
    method: "DELETE",
  });

  list.value = await $fetch("/api/news/list");
}
</script>

<template>
  <h1>News</h1>
  <ol>
    <li class="new">
      <button @click="addArticle" class="large-square-btn">
        <Plus></Plus>
      </button>
      <input type="date" v-model="add.date" />
      <input class="headline" v-model="add.headline" placeholder="Headline" />
    </li>
    <li v-for="article in list" :key="article.date">
      <button @click="removeArticle(article.date)" class="large-square-btn">
        <X></X>
      </button>
      <a :href="`/news/edit/${article.date}`">
        <span class="date">{{ new Date(article.date).toDateString() }} - </span>
        <span>{{ article.headline }}</span>
      </a>
    </li>
  </ol>
</template>

<style lang="css" scoped>
span,
input {
  font-size: 1.2em;
}

ol {
  padding: 0;
}

li {
  display: block;
  margin-top: 0.3em;
  margin-bottom: 0.3em;
  display: flex;
  gap: 0.7em;
}

.date {
  font-family: monospace;
}

.headline {
  width: 100%;
  max-width: 24em;
}

input {
  border-radius: 0;
  border: solid black 1px;
  box-sizing: border-box;
  max-height: 24px;
  padding-left: 0.3em;
}

.large-square-btn {
  width: 24px;
  height: 24px;
  min-width: 24px;
  min-height: 24px;
}

.large-square-btn > svg {
  width: 100%;
  height: 100%;
}
</style>
