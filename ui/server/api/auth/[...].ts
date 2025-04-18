import GithubProvider from "next-auth/providers/github";
import { NuxtAuthHandler } from "#auth";

export default NuxtAuthHandler({
  secret: process.env.AUTH_SECRET,
  providers: [
    // @ts-expect-error Use .default here for it to work during SSR.
    GithubProvider.default({
      clientId: process.env.CLIENT_ID,
      clientSecret: process.env.CLIENT_SECRET,
    }),
  ],
  callbacks: {
    async signIn({ user, account, profile }) {
      const role = await $fetch(
        `/api/role/${(profile as any).login as string}`,
      );

      return role && role != "unknown";
    },
    jwt({ token, account, profile }) {
      if (profile && account) {
        token.username = (profile as any).login as string;
        token.id = (profile as any).id as number;
      }

      return token;
    },
    async session({ session, token }) {
      const role = await $fetch(`/api/role/${token.username}`);
      return {
        ...session,
        user: {
          name: token.username as string,
          id: token.id,
          role,
        },
      };
    },
  },
});
