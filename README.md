# ClassicHostiles
Creature mod for minecraft 1.19

# Contribution rules:
 - Packages must use lower case only and no underscores
 - Classes are to be named like the 1.16 mojang naming scheme (NameType, e.g. PlayerEntity, AppleItem,...)
 - Each minecraft-version the mod is to be worked on a separate branches (e.g. 1.16 branch and 1.18 branch)
 - Tags and released are to be used to archive builds and the source code of those, a single "Version" branch is bad practice and shall not be used because of that
 - The version number might only increase directly before an update, that commit is to be called "version increment" and may only contain the changes to the files that contain the version number (normally gradle.properties and the mod main class)
 - Before the release gets published a tag with the same name as the version number is to be created, keep in mind to base it on the right branch
 - The master branch is to be removed, main branch always will be "dev"
 - The dev branch is to be for the latest minecraft version the mod supports, every other version has their own branch that is called like it's mc-version number
 - No magic numbers please, if it is preferable to use a magic number please make a comment on it! Magic strings (e.g. keys in NBT data) should be declared as a central constant and the constant should be used
 - Put a small description above the function if it is not trivial
 - Once something can be copied 1:1 => Move it to a method!
 - static, final and static-final fields are to be named in upper snake case (e.g. THIS_IS_A_DEMO), that is to clearly identify something as a constant
 - The progress is to be tracked using the existing github projects. If you choose to do one ticket: Assign yourself on the issue and comment on that to mark progress
