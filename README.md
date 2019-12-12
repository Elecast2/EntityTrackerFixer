# EntityTrackerFixer
Untrack entities that are not used at all by the server

- EntityTrackerFixer -


Did you try everything to fix the lag on your server 1.14.4 or 1.15 but it is still lagging?
Do timings indicate some random entity is causing lag?

This plugin may help you

How does it works?
[QUOTE]Minecraft tracks a lot of entities, even if they are outside the tracking range of the player, that's a normal behavior but is a tps killer for 1.14.4 and 1.15 servers with more than 30 players. So what this plugin do is untrack those entities every configured ticks and track them again if the player is near.[/QUOTE]

Report any bug in the discussion section.

________________________________________________________________

config.yml
[code=YAML]#Log when the plugin untrack entities
log-to-console: true

#disable tick for untracked entities (experimental, use at your own risk)
disable-tick-for-untracked-entities: false

#How many ticks between untrack process
#The untrack process will check for untracked entities by players but still by the server, and untrack them.
untrack-ticks: 500

#if tps are not below this value, the task will not perform the untrack and it will wait for the next run
tps-limit: 19.5

#frecuency in ticks to check untracked entities
#It will check for players traking entities and will track it again (this is to prevent invisible entities)
check-untracked-entities-frequency: 40

#Distance in blocks to check for players near untracked entities
tracking-range: 30

#which worlds do you want the plugin to take effect?
worlds:
  - world
  - world_nether
  - world_the_end[/code]

________________________________________


Do you like this plugin?
feel free to donate buying something
on my server store:
https://tienda.minemora.net/

________________________________________
