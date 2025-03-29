# Pearson B2C Hybris project

# Configuration
Set up steps: https://one-confluence.pearson.com/pages/viewpage.action?pageId=161389698

# Code standards
Code standards for this project can be found here: https://one-confluence.pearson.com/display/PMC/Hybris+Coding+Standards+and+Best+Practices

# Submodules
This project uses the following submodules:
- https://github.com/pearson-dnt-pmc/shared-hybris
- https://github.com/pearson-dnt-pmc/shared-hybris-integrations
- https://github.com/pearson-dnt-pmc/shared-hybris-external-integrations
- https://github.com/pearson-dnt-pmc/shared-pearsonbase
- https://github.com/pearson-dnt-pmc/qe-globalstore-ui-automation

# Useful scripts
Helpful groovy scripts and FlexibleSearch queries and can be found here:
- Locally in `./scripts` folder
- https://one-confluence.pearson.com/display/PMC/Learner+-+Helpful+Hybris+groovy+scripts
- https://one-confluence.pearson.com/display/PMC/Helpful+Hybris+FlexibleSearch+queries+for+AQA

# Build and start Hybris project without internet connection
If the Hybris project is built at least ones before, then it can be rebuilt without internet connection.
In order to do it you need to switch your local Hybris to use mocks as it's done for UI tests on CI pipeline.
Please take all properties from `./config/ci/ui/10-local.properties` file **except** properties with keys: `db.url` and `standalone.javaoptions`.
And override existing Hybris properties by them (Please find instruction below in section "Override some properties locally").

# Override some properties locally
1) Create new property file in any place on your local machine (let's imagine you created file by path: "/some/path/own.properties");
2) Put all properties that you want to override to the file;
3) Create environment variable with name: "HYBRIS_RUNTIME_PROPERTIES" and value: full path to the created file ("/some/path/own.properties");
4) Build and start the Hybris project

Notes:

On unix systems environment variable can be created in same terminal when your build and start Hybris by next cmd command:

`export HYBRIS_RUNTIME_PROPERTIES=/some/path/own.properties`

On Windows systems environment variable can be created through UI or be next cmd command:

`set HYBRIS_RUNTIME_PROPERTIES=/some/path/own.properties`

**Please** replace `/some/path/own.properties` by real full path you file that your created.
