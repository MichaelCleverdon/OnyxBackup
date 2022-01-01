// This software is not licensed to anybody and should not be used
const axios = require('axios');
const { Octokit } = require("@octokit/core");
const { createAppAuth } = require('@octokit/auth-app');
const express = require('express');
const fs = require('fs');
const draftEditor = require("./draftEditor")
const port = process.env.PORT || 8080

const pem = fs.readFileSync('./key.pem', 'utf8');

const octokit = new Octokit({
    authStrategy: createAppAuth,
    auth: {
        appId: process.env.APPID,
        privateKey: pem,
        installationId: process.env.INSTALLATIONID,
        clientId: process.env.CLIENTID,
        clientSecret: process.env.CLIENTSECRET,
    }
});

run().catch(err => console.log(err));

async function run() {
    const app = express();
    app.use(express.json());
    app.post('/data', function(req, res) {
        //Better console logging with ability to filter out things from the payload we don't want to log out
        if(req.body != null) {
            for(const [key, value] of Object.entries(req.body)) {
                if(key != 'comment' && key != 'repository')
                    console.log(`${key}: `, value);
            }

        };

        if (req.body != null && req.body.issue != null && req.body.action == "closed") {
            //This reads from the pull request body and not the merge commit
            const commentURLs = req.body.comments_url;
            const commentsJSON = githubRequest(commentURLs.substring(22), 14870904);
            const mergeMessage = null;

            if(commentsJSON != null) {
                commentsJSON.forEach(comments => {
                    if(comments.body.search("EXCLUDE!") != -1){
                        console.log("Exclude found, ignoring ticket.");
                        return;
                    }

                    const startIndex = comments.body.search("RELEASE:");
                    const endIndex = comments.body.search("ENDRELEASE");
                    if( startIndex != -1 && endIndex != -1 ) {
                        mergeMessage = comments.body.substring(startIndex+8, endIndex);
                    }
                });
            } 
            
            if( mergeMessage == null ) {
                console.log("Incorrect keyword syntax!"); 
                //This will only post comments if it doesn't find the keywords on an issue
                postComment(req.body.issue.repository_url, req.body.issue.number);
            } else {
                let payload = mergeMessage.split("\n")

                const repoTemplate = getTemplate("2021springcs471","GithubReleaseBot", 14870904)
                // if(repoTemplate != null) {
                //     console.log("Release template exists!")
                //     //var jsonData = JSON.parse(repoTemplate);
                //     //We need to decide how to proceed with this. The current version of releaseTemplate doesn't match up with our original draft of the default template info.
                // } else {
                //     console.log("Release template not found! Using commit body to decide layout.")
                // }

                let tags = processPayload(payload)
                let tag_name = "not_defined"
                if (tags['VERSION'])
                    tag_name = tags['VERSION']
                console.log(tags)
                //Draft exists
                draftEditor.doesReleaseDraftExist("2021springcs471", "GithubReleaseBot")
                    .then(value => {
                        if (value){
                            draftEditor.getDraftId("2021springcs471", "GithubReleaseBot")
                                .then(draftId => {
                                    draftEditor.getReleaseBody("2021springcs471", "GithubReleaseBot", draftId)
                                        .then(data => {
                                            draftEditor.templateAdder(data, tags['TYPE'], `[${tags['DESC']}](${req.body.issue.url})`)
                                                .then(tem =>{
                                                    if (tag_name === "not_defined"){
                                                        draftEditor.update_draft("2021springcs471", "GithubReleaseBot",draftId, tem)
                                                    }else{
                                                        draftEditor.update_draft("2021springcs471", "GithubReleaseBot",draftId, tem, tag_name)
                                                    }

                                                })
                                        })
                                    console.log("Draft updated")
                                })
                        }else{
                            getTemplateDE(function (template){
                                draftEditor.formatFromTemplate("./releaseTemplate.json")
                                .then(s => {
                                    draftEditor.templateAdder(s, tags['TYPE'], `[${tags['DESC']}](${req.body.issue.url})`)
                                        .then(tem =>{
                                            draftEditor.create_draft("2021springcs471", "GithubReleaseBot", tag_name, tem)
                                        })
                                })
                                console.log("Draft created")
                            })
                        }
                    })
            }
        };

        if (req.body != null && req.body.ref === 'refs/heads/master') {
            const installationId = req.body.installation.id;
            getPackageJSON(req.body.repository.full_name, installationId);
        };
        res.status(200).end();
    });

    app.listen(port, () => console.log(`Server is running!`));
}

async function getPackageJSON(repo, installationId) {
    const pkg = await githubRequest(`/repos/${repo}/contents/package.json`, installationId).
    then(res => res.content).
    then(content => Buffer.from(content, 'base64').toString('utf8'));
    console.log(JSON.parse(JSON.stringify(pkg))); //Make use of the JSON.parse method for console logging allowing elements to be grabbed by ID -- May need to use assign to variable
    //console.log('package.json:', pkg);
}

async function postComment(repoUrl, issue_number){
    const pkg = await draftEditor.githubPost(repoUrl, issue_number, "You do not have the required keywords to use the release draft bot."+
    "\n Please add 'RELEASE:' and 'ENDRELEASE' to your pull request comment in order to use the release drafting functionality").then(function(response){
        console.log("success");
    }).catch(function(error){
        console.log("error: " +error);
    }).then(function(){
        console.log("At least something happened");
    }); //TODO: Make a better comment
}

async function getTemplate(owner, repo, installationId){
    const token = await createJWT(installationId);
    const res = await axios({
        method: 'get',
        url: `https://api.github.com/repos/${owner}/${repo}/contents/releaseTemplate.json`,
        headers: {
            authorization: `bearer ${token}`,
        }
    });
    return res.data;
}

async function createJWT(installationId) {
    const auth = createAppAuth({
        appId: process.env.APPID,
        privateKey: pem,
        installationId: installationId,
        clientId: process.env.CLIENTID,
        clientSecret: process.env.CLIENTSECRET,
    });

    const { token } = await auth({ type: 'installation' });
    return token;
}

async function githubRequest(url, installationId, method, data) {
    const token = await createJWT(installationId);
    if (method == null) {
        method = 'get';
    } else {
        method = method.toLowerCase();
    }
    const accept = url.includes('/check-runs') ?
        'application/vnd.github.antiope-preview+json' :
        'application/vnd.github.machine-man-preview+json';
    const res = await axios({
        method,
        url: `https://api.github.com${url}`,
        data,
        headers: {
            authorization: `bearer ${token}`,
            accept
        }
    });
    return res.data;
}

function processPayload(payload){
    let tags = {}
    payload.forEach(s => {
        //console.log(s.replace("\r",''))
        let delimiter = s.indexOf(":")
        if (delimiter === -1) {
            //console.log("Missing delimiter in a key/value pair: " + s)
            return
        }
        let key = s.substring(0,delimiter)
        let value = s.substring(delimiter+1,s.length).trim()
        tags[key] = value
        //console.log(`Key: ${key}\nValue: ${value}`)
    })
    return tags
}

async function getTemplateDE(callback){
    draftEditor.getCommitList("2021springcs471", "GithubReleaseBot")
        .then(res =>{
            let commits = res.data
            commits.every(function (data){
                if(data.commit.message.indexOf("releaseTemplate.json") > -1){
                    draftEditor.getCommit("2021springcs471", "GithubReleaseBot", data.sha)
                        .then(res => {
                            let tree_sha = res.data.commit.tree.sha
                            draftEditor.getTree("2021springcs471", "GithubReleaseBot", tree_sha)
                                .then(res =>{
                                    res.data.tree.every(function (branch){
                                        if (branch.path.indexOf("releaseTemplate.json") > -1){
                                            let sha = branch.sha
                                            draftEditor.getFileContents("2021springcs471", "GithubReleaseBot", sha)
                                                .then(res => {
                                                    let buff = new Buffer(res.data.content, 'base64')
                                                    let text = JSON.parse(buff.toString('ascii'))
                                                    callback(text)
                                                })
                                            return false
                                        }else{
                                            return true
                                        }
                                    })
                                })
                        })
                    return false
                }else{
                    return true
                }
            })
        })
}
