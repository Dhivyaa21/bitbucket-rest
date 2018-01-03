
[![Build Status](https://travis-ci.org/cdancy/bitbucket-rest.svg?branch=master)](https://travis-ci.org/cdancy/bitbucket-rest)
[![codecov](https://codecov.io/gh/cdancy/bitbucket-rest/branch/master/graph/badge.svg)](https://codecov.io/gh/cdancy/bitbucket-rest)
[![Download](https://api.bintray.com/packages/cdancy/java-libraries/bitbucket-rest/images/download.svg) ](https://bintray.com/cdancy/java-libraries/bitbucket-rest/_latestVersion)
[![Stack Overflow](https://img.shields.io/badge/stack%20overflow-bitbucket&#8211;rest-4183C4.svg)](https://stackoverflow.com/questions/tagged/bitbucket+rest)

# bitbucket-rest
![alt tag](https://wac-cdn.atlassian.com/dam/jcr:e2a6f06f-b3d5-4002-aed3-73539c56a2eb/bitbucket_rgb_blue.png?cdnVersion=cm)

java client, based on jclouds, to interact with Bitbucket's REST API. 


## On jclouds, apis and endpoints
Being built on top of `jclouds` means things are broken up into [Apis](https://github.com/cdancy/bitbucket-rest/tree/master/src/main/java/com/cdancy/bitbucket/rest/features). 
`Apis` are just Interfaces that are analagous to a resource provided by the server-side program (e.g. /api/branches, /api/pullrequest, /api/commits, etc..). 
The methods within these Interfaces are analagous to an endpoint provided by these resources (e.g. GET /api/branches/my-branch, GET /api/pullrequest/123, DELETE /api/commits/456, etc..). 
The user only needs to be concerned with which `Api` they need and then calling its various methods. These methods, much like any java library, return domain objects 
(e.g. POJO's) modeled after the json returned by `bitbucket`. 

Interacting with the remote service becomes transparent and allows developers to focus on getting things done rather than the internals of the API itself, or how to build a client, or how to parse the json. 

## On new features

New Api's or endpoints are generally added as needed and/or requested. If there is something you want to see just open an ISSUE and ask or send in a PullRequest. However, putting together a PullRequest for a new feature is generally the faster route to go as it's much easier to review a PullRequest than to create one ourselves. There is no problem doing so of course but if you need something done now than a PullRequest is your best bet otherwise you may have to patiently wait for one of our contributors to take up the work.

## Latest release

Can be sourced from jcenter like so:

    <dependency>
      <groupId>com.cdancy</groupId>
      <artifactId>bitbucket-rest</artifactId>
      <version>X.Y.Z</version>
      <classifier>sources|tests|javadoc|all</classifier> (Optional)
    </dependency>
	
## Documentation

javadocs can be found via [github pages here](http://cdancy.github.io/bitbucket-rest/docs/javadoc/)

## Property based setup

Client's do NOT need supply the endPoint or credentials as part of instantiating the BitbucketClient object. 
Instead one can supply them through system properties, environment variables, or a combination 
of the 2. System properties will be searched first and if not found we will attempt to 
query the environment.

Setting the `endpoint` can be done with any of the following (searched in order):

- `bitbucket.rest.endpoint`
- `bitbucketRestEndpoint`
- `BITBUCKET_REST_ENDPOINT`

Setting the `credentials` can be done with any of the following (searched in order):

- `bitbucket.rest.credentials`
- `bitbucketRestCredentials`
- `BITBUCKET_REST_CREDENTIALS`

Setting the `token` can be done with any of the following (searched in order):

- `bitbucket.rest.token`
- `bitbucketRestToken`
- `BITBUCKET_REST_TOKEN`

## Authentication

Authentication/Credentials for `bitbucket-rest` can take 1 of 3 forms:

- Colon delimited username and password: __admin:password__
- Base64 encoded username and password: __YWRtaW46cGFzc3dvcmQ=__
- Personal access token: __9DfK3AF9Jeke1O0dkKX5kDswps43FEDlf5Frkspma21M__

## Examples on building a client

When using `Basic` (e.g. username and password) authentication:

    BitbucketClient client = BitbucketClient.builder()
    .endPoint("http://127.0.0.1:7990") // Optional and can be sourced from system/env. Falls back to http://127.0.0.1:7990
    .credentials("admin:password") // Optional and can be sourced from system/env.
    .build();

    Version version = client.api().systemApi().version();

When using `Bearer` (e.g. token) authentication:

    BitbucketClient client = BitbucketClient.builder()
    .endPoint("http://127.0.0.1:7990") // Optional and can be sourced from system/env. Falls back to http://127.0.0.1:7990
    .token("123456789abcdef") // Optional and can be sourced from system/env.
    .build();

    Version version = client.api().systemApi().version();

When using anonymous authentication or sourcing from system/environment:

    BitbucketClient client = BitbucketClient.builder()
    .endPoint("http://127.0.0.1:7990") // Optional and can be sourced from system/env. Falls back to http://127.0.0.1:7990
    .build();

    Version version = client.api().systemApi().version();

## Understanding Error objects

When something pops server-side `bitbucket` will hand us back a list of [Error](https://github.com/cdancy/bitbucket-rest/blob/master/src/main/java/com/cdancy/bitbucket/rest/error/Error.java) objects. Instead of failing and/or throwing an exception at runtime we attach this List of `Error` objects 
to most [domain](https://github.com/cdancy/bitbucket-rest/tree/master/src/main/java/com/cdancy/bitbucket/rest/domain) objects. Thus, it is up to the user to check the handed back domain object to see if the attached List is empty, and if not, iterate over the `Error` objects to see if it's something 
truly warranting an exception. List of `Error` objects itself will always be non-null but in most cases empty (unless something has failed).

An example on how one might proceed:

    PullRequest pullRequest = client.api().pullRequestApi().get("MY-PROJECT", "MY-REPO", 99999);
    if (pullRequest.errors().size() > 0) {
        for(Error error : pullRequest.errors()) {
            if (error.message().matches(".*Pull request \\d+ does not exist in .*")) {
                throw new RuntimeException(error.message());
            }
        }
    }


## Examples

The [mock](https://github.com/cdancy/bitbucket-rest/tree/master/src/test/java/com/cdancy/bitbucket/rest/features) and [live](https://github.com/cdancy/bitbucket-rest/tree/master/src/test/java/com/cdancy/bitbucket/rest/features) tests provide many examples
that you can use in your own code.

## Components

- jclouds \- used as the backend for communicating with Bitbucket's REST API
- AutoValue \- used to create immutable value types both to and from the bitbucket program
    
## Testing

Running mock tests can be done like so:

    ./gradlew mockTest
	
Running integration tests can be done like so (requires Bitbucket instance):

    ./gradlew integTest
	
# Additional Resources

* [Bitbucket docker setup](https://bitbucket.org/atlassian/docker-atlassian-bitbucket-server)
* [Bitbucket REST API](https://developer.atlassian.com/static/rest/bitbucket-server/latest/bitbucket-rest.html)
* [Bitbucket Auth API](https://developer.atlassian.com/bitbucket/server/docs/latest/how-tos/example-basic-authentication.html)
* [Apache jclouds](https://jclouds.apache.org/start/)
