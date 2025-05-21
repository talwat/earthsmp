<script setup lang="ts">
import { validate as isValidUUID } from "uuid";
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
  id: number | undefined;
  ruler: string;
  members: string[];
  territories: Territory[];
}

interface Nations {
  nations: Nation[];
}

function url(): string {
  return `/api/nations.yml?t=${Date.now()}&encoding=utf8`;
}

let nations = ref(await content());
let search = ref("");
let filtered = computed(() =>
  nations.value.nations.filter(
    (x) =>
      x.nick == undefined ||
      x.nick.toLowerCase().includes(search.value.toLowerCase()) ||
      x.tag.toLowerCase() == search.value.toLowerCase(),
  ),
);
let changed = ref(false);

// @ts-ignore-error
const userCache: Record<string, string> = await $fetch("/api/usercache");

async function content(): Promise<Nations> {
  const text = (await $fetch(url())) as string;
  const parsed: Nations = yaml.parse(text);

  parsed.nations.map((x, i) => {
    x.id = i;
  });

  return parsed;
}

// Refresh every 30 seconds.
setInterval(async () => {
  if (changed.value == false) {
    nations.value = await content();
  }
}, 30000);

async function upload() {
  const required = ["tag", "nick", "name", "color"];
  for (const nation of nations.value.nations) {
    for (const key of required) {
      const value = nation[key as keyof Nation];
      if (value == undefined || value.toString().length == 0) {
        alert(`Field ${key} for ${nation.tag}/${nation.name} is missing!`);
        return;
      }
    }
  }

  await $fetch("/api/upload/nations.yml?encoding=utf8", {
    method: "POST",
    body: yaml.stringify(nations.value),
  });

  nations.value = await content();
  changed.value = false;
  alert("Changes uploaded!");
}

function hsl(hue: number, saturation: number) {
  return `hsl(${hue}, ${saturation}%, 50%)`;
}

function addMember(nation: Nation) {
  changed.value = true;

  if (nation.members == undefined) {
    nation.members = [];
  }

  nation.members.push("");
}

function removeMember(nation: Nation, i: number) {
  changed.value = true;

  let member = nation.members[i];
  if (userCache[member]) {
    member = `${member} (${userCache[member]})`;
  }

  if (
    !isValidUUID(nation.members[i]) ||
    confirm(`Are you sure you'd like to kick ${member} from ${nation.nick}?`)
  ) {
    nation.members.splice(i, 1);
  }
}

function removeTerritory(nation: Nation, i: number) {
  if (
    confirm(`Are you sure you'd like to remove ${nation.territories[i].name}?`)
  ) {
    nation.territories.splice(i, 1);
    changed.value = true;
  }
}

function removeNation(i: number) {
  let nation = nations.value.nations[i];

  if (confirm(`Are you sure you'd like to remove ${nation.nick}?`)) {
    nations.value.nations.splice(i, 1);
    changed.value = true;
  }
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
  let validUUID = isValidUUID(nation.members[i]);
  if (nation.members[i].length == 0 || !validUUID) {
    nation.members.splice(i, 1);
  }

  if (!validUUID) {
    alert(
      "Usernames can only be used for people who have already joined the server. Please use a UUID.",
    );
  }
}
</script>

<template>
  <h1>Nations</h1>
  <div @click="upload()" v-if="changed" class="save-container">
    <button class="save-btn">Save Changes</button>
  </div>
  <div class="search-container">
    <label for="search">Search</label>
    <input id="search" v-model="search" placeholder="Nick or Tag" />
  </div>
  <div class="nations-container" @input="changed = true">
    <div class="nation" v-for="nation in filtered" :key="nation.id">
      <div class="title-container">
        <h2 class="nation-title">
          <pre>{{ nation.tag }}</pre>
          - {{ nation.nick }}
        </h2>
        <button
          class="large-square-btn nation-remove"
          @click="removeNation(nation.id!)"
        >
          <X></X>
        </button>
      </div>
      <table>
        <tbody>
          <tr>
            <th>Tag</th>
            <td>
              <input class="code" placeholder="xxx" v-model="nation.tag" />
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
                    <button class="square-btn" @click="removeMember(nation, i)">
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
                <li v-for="(territory, i) in nation.territories" :key="i">
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
                          @click="removeTerritory(nation, i)"
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
                        changed = true;

                        if (!nation.territories) {
                          nation.territories = [];
                        }

                        nation.territories.push({
                          color: 0,
                          colony: false,
                        } as Territory);
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

    <div class="add-nation-container">
      <label for="add-nation">Add Nation</label>
      <button
        id="add-nation"
        class="large-square-btn"
        @click="
          () => {
            changed = true;
            nations.nations.push({
              color: 0,
              id: nations.nations.length,
            } as Nation);
          }
        "
      >
        <Plus></Plus>
      </button>
    </div>
  </div>
</template>

<style lang="css" scoped>
.add-nation-container {
  display: flex;
  align-items: center;
  flex-direction: row;
  height: fit-content;
}

.nations-container {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(500px, 1fr));
  gap: 1em;
}

.save-container {
  align-items: center;
  display: flex;
  flex-direction: column;
  position: fixed;
  width: 100vw;
  left: 0;
  top: 5em;
}

.save-btn {
  font-size: 1.15em;
  margin-bottom: 1em;
}

.nation {
  width: 100%;
  max-width: calc(100vw - 2em);
}

input[type="checkbox"] {
  width: auto !important;
}

.search-container {
  margin-bottom: 1em;
}

label {
  margin-right: 0.5em;
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
  margin: 0;
  padding: 0;
}

.title-container {
  display: flex;
  width: 100%;
  justify-content: space-between;
  border: 1px solid black;
  align-items: center;
  box-sizing: border-box;
  border-bottom: 0;
  padding-left: 0.3em;
  padding-right: 0;
}

.nation-remove {
  border: 0;
}

.member-ruler {
  text-decoration: underline;
}

.nations-container input {
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
