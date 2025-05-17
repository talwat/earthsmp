// https://nuxt.com/docs/api/configuration/nuxt-config
export default defineNuxtConfig({
  compatibilityDate: "2024-11-01",
  devtools: { enabled: false },
  ssr: false,
  modules: ["nuxt-auth-utils"],
  runtimeConfig: {
    session: {
      name: "nuxt-session",
      password: process.env.NUXT_SESSION_PASSWORD || "",
      cookie: {
        sameSite: "lax",
        secure: false,
      },
    },
  },
});
