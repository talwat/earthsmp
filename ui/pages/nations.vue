<script setup lang="ts">
import type { UUID } from "crypto";
import yaml from "yaml";

interface Territory {
  tag: string;
  name: string;
  color: number;
  colony: boolean;
}

interface Nation {
  tag: string;
  name: string;
  nick: string;
  color: number;
  flag: string;
  ruler: string;
  members: string[];
  territories: Territory[];
}

interface Nations {
  nations: Nation[];
}

const url = ref(`/api/nations.yml?t=${Date.now()}&encoding=utf8`);
const raw: Nations = await content();
raw.nations.sort((a, b) => (b.members?.length || 0) - (a.members?.length || 0));
const nations = ref(raw);

const userCache: Record<string, string> = await $fetch("/api/usercache");

async function content() {
  const text = (await $fetch(url.value)) as string;
  const parsed = yaml.parse(text);

  return parsed;
}

function hsl(hue: number, saturation: number) {
  return `hsl(${hue}, ${saturation}%, 50%)`;
}

function addMember(nation: Nation) {
  nation.members.push("" as UUID);
}

function memberChange(nation: Nation, i: number) {
  let cacheEntries: [string, string][] = Object.entries(userCache);
  let cache = cacheEntries.find(
    (value) => value[1].toLowerCase() == nation.members[i].toLowerCase(),
  );
  if (cache) {
    nation.members[i] = cache[0];
  }
}

function memberBlur(nation: Nation, i: number) {
  if (nation.members[i].length == 0) {
    nation.members.splice(i, 1);
  }
}
</script>

<template>
  <h1>Nations</h1>
  <div class="nations-container">
    <div class="nation" v-for="nation in nations.nations">
      <h2 class="nation-title">
        <pre>{{ nation.tag }}</pre>
        - {{ nation.nick }}
      </h2>
      <table>
        <tbody>
          <tr>
            <th>Tag</th>
            <td>
              <input class="code" v-model="nation.tag" />
            </td>
          </tr>
          <tr>
            <th>Common Name</th>
            <td><input v-model="nation.nick" /></td>
          </tr>
          <tr>
            <th>Full Name</th>
            <td><input v-model="nation.name" /></td>
          </tr>
          <tr>
            <th>Color</th>
            <td class="color-container">
              <div
                class="box"
                :style="{ backgroundColor: hsl(nation.color, 100) }"
              ></div>
              <input v-model="nation.color" />
            </td>
          </tr>
          <tr>
            <th>Flag</th>
            <td>
              <input class="code" v-model="nation.flag" placeholder="No Flag" />
            </td>
          </tr>
          <tr>
            <th>Members</th>
            <td>
              <ul>
                <li v-for="(member, i) in nation.members">
                  <div class="player-container">
                    <button
                      class="square-btn"
                      @click="nation.members.splice(i, 1)"
                    >
                      <X></X>
                    </button>

                    <!-- placeholder="00000000-0000-0000-0000-000000000000" -->
                    <input
                      @blur="memberBlur(nation, i)"
                      @input="memberChange(nation, i)"
                      class="code uuid"
                      placeholder="UUID or Username"
                      v-model="nation.members[i]"
                      :class="{ 'member-ruler': nation.ruler == member }"
                    />
                    <span v-if="userCache[member]" class="player-name">{{
                      userCache[member]
                    }}</span>
                    <span v-else class="disabled player-name">Unknown</span>
                  </div>
                </li>
                <li>
                  <button class="square-btn" @click="addMember(nation)">
                    <Plus></Plus>
                  </button>
                </li>
              </ul>
            </td>
          </tr>
          <tr>
            <th>Ruler</th>
            <td class="player-container">
              <input
                class="code uuid"
                v-model="nation.ruler"
                placeholder="No Ruler"
              />
              <span v-if="userCache[nation.ruler]" class="player-name">{{
                userCache[nation.ruler]
              }}</span>
              <span v-else-if="nation.ruler" class="disabled player-name"
                >Unknown</span
              >
            </td>
          </tr>
          <tr>
            <th>Territories</th>
            <td class="territory-data">
              <ul class="territory-list">
                <li v-for="(territory, i) in nation.territories">
                  <table class="territory-table">
                    <tbody>
                      <tr rowspan="2">
                        <th>Name</th>
                        <td><input v-model="nation.territories[i].name" /></td>
                      </tr>
                      <tr rowspan="2">
                        <th>Tag</th>
                        <td>
                          <input
                            v-model="nation.territories[i].tag"
                            class="code"
                          />
                        </td>
                      </tr>
                      <tr rowspan="2">
                        <th>Colony</th>
                        <td>
                          <input
                            type="checkbox"
                            v-model="nation.territories[i].colony"
                          />
                        </td>
                      </tr>
                      <tr rowspan="1">
                        <th>Color</th>
                        <td class="color-container">
                          <div
                            class="box"
                            :style="{
                              backgroundColor: hsl(
                                nation.color,
                                territory.color,
                              ),
                            }"
                          ></div>
                          <input v-model="nation.territories[i].color" />
                        </td>
                        <td
                          class="territory-remove"
                          @click="nation.territories.splice(i, 1)"
                        >
                          <X></X>
                        </td>
                      </tr>
                    </tbody>
                  </table>
                </li>
                <li>
                  <button
                    class="large-square-btn"
                    @click="
                      () => {
                        if (!nation.territories) {
                          nation.territories = [];
                        }

                        nation.territories.push({ color: 0 } as Territory);
                      }
                    "
                  >
                    <Plus></Plus>
                  </button>
                </li>
              </ul>
            </td>
          </tr>
        </tbody>
      </table>
    </div>
  </div>
</template>

<style lang="css" scoped>
.nations-container {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 1em;
}

.nation {
  width: 100%;
}

input[type="checkbox"] {
  width: auto;
}

ul {
  margin-top: 8px;
  margin-bottom: 8px;
  padding-left: 20px;
}

.territory-remove {
  border-left: 1px solid black;
  text-align: center;
  padding: 0;
  height: 32px;
  width: 32px;
  cursor: pointer;
  vertical-align: middle;
}

.territory-remove > svg {
  width: 50%;
  height: 50%;
  display: block;
  margin: 0 auto;
}

.territory-remove:hover {
  background-color: lightgray;
}

.territory-table {
  margin: 0;
  margin-right: 1em;
  width: calc(100% - 20px);
}

.territory-list > li:not(:last-child) {
  margin-bottom: 0.5em;
}

.nation-title {
  border: 1px solid black;
  border-bottom: 0;
  margin: 0;
  padding: 0.2em;
}

.member-ruler {
  text-decoration: underline;
}

input {
  width: 100%;
  box-sizing: border-box;
  border: none;
  padding: 0;
  margin: 0;
}

.code {
  font-family: monospace;
}

.disabled {
  color: gray;
}

pre {
  overflow: scroll;
  margin: 0;
  white-space: pre-wrap;
  display: inline;
}

table {
  border: 1px solid;
  border-collapse: collapse;
  width: 100%;
}

tr {
  border-bottom: 1px solid black;
  height: 32px;
}

th {
  padding: 0.3em;
  border-right: 1px solid black;
  text-align: left;
  width: 8em;
}

td {
  padding: 0.3em;
}

.square-btn {
  width: 20px;
  height: 20px;
  min-width: 20px;
  min-height: 20px;
  line-height: 20px;
  text-align: center;
}

.square-btn > svg {
  width: 100%;
  height: 100%;
  vertical-align: top;
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

.player-name {
  font-style: italic;
}

.player-container {
  gap: 0.6em;
  display: flex;
  margin-right: 0.7em;
  align-items: center;
}

li {
  min-height: 1.5em;
}

.uuid {
  max-width: 36ch;
  overflow: scroll;
}

.color-container {
  display: flex;
  flex-direction: row;
  align-items: center;
  gap: 0.5em;
  padding: 0;
  padding-left: 8px;
  height: inherit;
}

.box {
  height: 20px;
  min-width: 20px;
}
</style>
