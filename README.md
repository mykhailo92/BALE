# BALE



## Getting started

To make it easy for you to get started with GitLab, here's a list of recommended next steps.

Already a pro? Just edit this README.md and make it your own. Want to make it easy? [Use the template at the bottom](#editing-this-readme)!

## Add your files

- [ ] [Create](https://docs.gitlab.com/ee/user/project/repository/web_editor.html#create-a-file) or [upload](https://docs.gitlab.com/ee/user/project/repository/web_editor.html#upload-a-file) files
- [ ] [Add files using the command line](https://docs.gitlab.com/ee/gitlab-basics/add-file.html#add-a-file-using-the-command-line) or push an existing Git repository with the following command:

```
cd existing_repo
git remote add origin https://gitlab.uni-oldenburg.de/zake5362/bale.git
git branch -M main
git push -uf origin main
```

## How to Build jar (Intellij)
 
```
Open "Project Structure" 
Under Menu Point "Project Settings" select "Artifacts"
In the Left Column, select "+"
Select JAR and then select "From modules with dependencies..."
Select Main class (de.bale.Main)
Under "Jar Files From Libraries" select "Extract to the target JAR"
In the Right Column that opens select the <PROJECTNAME> Folder, which contains Maven Libraries
Drag & Drop the Maven Libraries into the bale.jar in the left column
Click OK
In the Intellij Menu select "Build->Build Artifacts..." and choose Build if prompted
The JAR will be generated in the "out" Folder
```