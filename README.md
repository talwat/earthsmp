# Talwat's EarthSMP plugin

This is the plugin used for Talwat's EarthSMP as a replacement for Towny, and several other plugins.

Currently, it's only compatible with [paper](https://papermc.io/)
on Minecraft version 1.19.4.
It also requires the server have [squaremap](https://github.com/jpenilla/squaremap) installed, as it uses it to display borders.

## Features

* Borders, defined by an image file (`borders.png`) rather than arbitrary chunks.

* [Custom recipes](RECIPES.md), designed to add alternative ways of accessing key items,
or making certain building blocks cheaper to encourage larger cities and builds.

* Grief protection, similar to Towny, where claimed chunks can only be interacted with by
the owners of the territory.

* Custom map markers, with icons to denote key locations, such as ports, cities, train stations, etc...

* Simplistic news system, whereby editing a file in a directory you can "publish" news
broadcasts, accessible by players with the `/earth news` command.

* Nation system, also similar to Towny, where territory is owned by different nations
that control territory and have members, a leader, and a flag (spawnable with `/earth flag`)

* Basic "signing" command, where a player can add a signature to an item that cannot be
forged or removed. This is useful for economies, where various items can be used and then
signed to act as currency.

## Ideology

The Ideology behind the plugin is to reduce the amount of control the server has as much as possible.
Instead, things such as economy, war, and transport should be dictated by player-setup systems rather
than built in shops, currencies, and teleport commands. This really helps immersion, and provides a
layer of "realism" by players having to set up these systems themselves.

## Commands

### Accessible to everyone

#### `/earth accept <tag>`

Accept a nation invite. You need to include the tag of the nation you want to join.
This is to prevent people from joining nations they don't want to join.

#### `/earth decline`

Decline a nation invite.

#### `/earth leave`

Leave your nation.

#### `/earth member list`

Get a list of members in your nation.

#### `/earth sign`

Sign an item, the signature can't be forged and can't be overwritten.

### Accessible to nation leaders only

#### `/earth flag`

Get 16 of your *nation's* flag.

#### `/earth abdicate <new leader>`

Transfer control of your nation to someone else in your nation.

#### `/earth marker new <label> <type>`

Make a new marker. The available types are: `capital`, `city`, `lumber`, `port`, `station`, `fort`, and `mine`.

#### `/earth marker delete <index>`

Deletes a marker. If you want to find out the index, use `/earth marker list`.
Indexes shift every time you delete though, so you'll have to check the list each
time you want to delete a marker.

#### `/earth member add <username>`

Sends a join invitation to a user.

#### `/earth member kick <username>`

Kicks a user.

## Usage Details

This plugin's state is entirely defined by a few yaml files and `borders.png` in the plugin's
directory. This is usually `plugins/EarthSMP`. These contain the data for borders,
countries, markers, and everything else.

### Borders

The `borders.png` file is the most challenging to work with. When setting it up,
remember that one pixel of the image is one chunk in game. Additionally,
the center of the image corresponds with chunk (0, 0).

You'll have to play around with it a bit to get a feel for it.
Grayscale pixels are also completely ignored, so you may use a grayscale
overlay of the Earth map you're using to give you a guide when drawing borders.

### Nations

Unlike Towny, this plugin doesn't have "towns", only nations.
Nations are defined in `nations.yml`, and while there exist *some*
commands to create new nations, you'll still have to edit this file
regularly.

Comments will, unfortunately, not be preserved in this file, but you can
refer to the [original commented file](https://github.com/talwat/earthsmp/blob/main/src/main/resources/nations.yml)
for field definitions and such.

### Markers

These are both relatively self-explanatory and do not need to be managed
by the admins. Rather, nation leaders are able to create markers by themselves
assigned to their nation.
