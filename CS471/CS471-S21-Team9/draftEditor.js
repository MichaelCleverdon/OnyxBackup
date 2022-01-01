const fs = require('fs')
const { Octokit } = require("@octokit/core");
const { createAppAuth } = require('@octokit/auth-app');
const pem = fs.readFileSync('./key.pem', 'utf8');

const octokit = new Octokit({
    authStrategy: createAppAuth,
    auth: {
        appId: process.env.APPID,
        privateKey: pem,
        installationId: process.env.INSTALLATIONID,
        clientId: process.env.CLIENTID,
        clientSecret: process.env.CLIENTSECRET,
    },
});

module.exports = {
    create_draft: async function(owner, repo, tag_name, body){
        const res = await octokit.request(`POST /repos/${owner}/${repo}/releases`, {
            owner: owner,
            repo: owner+"/"+repo,
            tag_name: tag_name,
            name: "Release Notes Draft",
            draft: true,
            body: body
        })
        return res.data.id
    },
    update_draft: async function(owner, repo, release_id, body, tag_name=null){
        if (tag_name == null){
            const res = await octokit.request(`PATCH /repos/${owner}/${repo}/releases/${release_id}`, {
                owner: owner,
                repo: owner+"/"+repo,
                draft: true,
                release_id: release_id,
                body: body
            })
            return res
        }else{
            const res = await octokit.request(`PATCH /repos/${owner}/${repo}/releases/${release_id}`, {
                owner: owner,
                repo: owner+"/"+repo,
                draft: true,
                release_id: release_id,
                body: body,
                tag_name: tag_name
            })
            return res
        }
    },

    formatFromTemplate: async function(template){
        let sectionActions = {
            'Features': "This will be grabbed from stored data?",
            'Bugs': 'This will be grabbed from stored data?'
        }
        let s = "# Github Release Bot \n---\n"
        Object.keys(template).forEach(section => {
            s+="## Section: "+section+"\n"
            s+=sectionActions[section]
            s+="\n"
        })
        return s
    },

    getCommit: async function(owner, repo, ref){
        const res = await octokit.request(`GET /repos/${owner}/${repo}/commits/${ref}`, {
            owner: owner,
            repo: owner+"/"+repo,
            ref: ref
        })

        return res
    },

    getDraftId: async function(owner, repo){
        const res = await octokit.request(`GET /repos/${owner}/${repo}/releases`, {
            owner: owner,
            repo: repo
        })
        return res.data[0].id
    },


    doesReleaseDraftExist: async function(owner, repo){
        const res = await octokit.request(`GET /repos/${owner}/${repo}/releases`, {
            owner: owner,
            repo: repo
        })
        let draft = false
        res.data.forEach(release => {
            if (release.draft){
                draft = true
            }
        })
        return draft
    },

    githubPost: async function(repoUrl, issue_number, data){
        let ownerAndRepo = repoUrl.substring(23);
        let owner = ownerAndRepo.split('/')[1];
        let repo = ownerAndRepo.split('/')[2];
        console.log(`/repos/${owner}/${repo}/issues/${issue_number}/comments`);
        const res = await octokit.request(`POST /repos/{owner}/{repo}/issues/{issue_number}/comments`, {
            owner: owner,
            repo: repo,
            issue_number: issue_number,
            body: data
        });
        return res;
    },
    templateAdder: async function(template, section, message){
        template = template.split("\n")
        let startIndex;
        switch (section){
            case "v":
                startIndex = template.indexOf("## Section: Version")
                break;
            case "f":
                startIndex = template.indexOf("## Section: Features")
                break;
            case "b":
                startIndex = template.indexOf("## Section: Bugs")
                break;
        }
        //if the section is empty
        if (template[startIndex+1] === "None Defined"){
            template[startIndex+1] = message
            //console.log(template)
        }else{
            //Shift everything down 1
            for(let i = template.length; i > startIndex; i--){
                template[i+1] = template[i]
                if (i === startIndex+1){
                    template[i] = message
                }
            }
            //console.log(template)
        }
        let s = "";
        template.forEach(line =>{
            if (s !== undefined){
                s+=line
                s+="\n"
            }
        })
        return s
    },
    
        getFileContents: async function(owner, repo, file_sha){
        const res = await octokit.request(`GET /repos/${owner}/${repo}/git/blobs/${file_sha}`, {
            owner: owner,
            repo: repo,
            file_sha: file_sha,
            accept: "application/vnd.github.v3+json"
        })
        return res
    },

    getCommit: async function(owner, repo, ref){
        const res = await octokit.request(`GET /repos/${owner}/${repo}/commits/${ref}`, {
            owner: owner,
            repo: repo,
            ref: ref
        })
        return res
    },

    getTree: async function(owner, repo, ref){
        const res = await octokit.request(`GET /repos/${owner}/${repo}/git/trees/${ref}`, {
            owner: owner,
            repo: repo,
            ref: ref
        })
        return res
    },

    getCommitList: async function(owner, repo){
        const res = await octokit.request(`GET /repos/${owner}/${repo}/commits`, {
            owner: owner,
            repo: repo,
        })
        return res
    },

}
