// https://nuxt.com/docs/api/configuration/nuxt-config
export default defineNuxtConfig({
  compatibilityDate: "2024-11-01",
  devtools: { enabled: true },
  //ssr: false,
  modules: ["@sidebase/nuxt-auth"],
  auth: {
    globalAppMiddleware: true,
    provider: {
      type: "authjs",
      defaultProvider: "github",
      addDefaultCallbackUrl: true,
    },
  },
});
