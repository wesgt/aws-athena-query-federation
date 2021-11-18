# Amazon Athena UDF Connector

This connector extends Amazon Athena's capability by adding customizable UDFs via Lambda.

## Supported UDFs

1. "pnp_region_number": parse a number and return a `region:num` string
 
Example query:

`USING EXTERNAL FUNCTION pnp_region_number(number VARCHAR, region VARCHAR) RETURNS VARCHAR LAMBDA 'numberudf' SELECT pnp_region_number('0223681234', 'TW')`

This would return result 'TW,886223681234'.

*To use the Athena Federated Query feature with AWS Secrets Manager, the VPC connected to your Lambda function should have [internet access](https://aws.amazon.com/premiumsupport/knowledge-center/internet-access-lambda-function/) or a [VPC endpoint](https://docs.aws.amazon.com/secretsmanager/latest/userguide/vpc-endpoint-overview.html#vpc-endpoint-create) to connect to Secrets Manager.

## AWS built UDFs
For an example that uses UDFs with Athena to translate and analyze text, see the AWS
                                    Machine Learning Blog article <a href="http://aws.amazon.com/blogs/machine-learning/translate-and-analyze-text-using-sql-functions-with-amazon-athena-amazon-translate-and-amazon-comprehend/" rel="noopener noreferrer" target="_blank"><span>Translate and analyze text using SQL functions with Amazon Athena, Amazon Translate,
                                          and Amazon Comprehend</span></a>, or watch the <a href="#udf-videos-xlate">video</a>

### Repositories for AWS built UDFs

1. https://github.com/aws-samples/aws-athena-udfs-textanalytics

## Deploying The Connector

To use this connector in your queries, navigate to AWS Serverless Application Repository and deploy a pre-built version of this connector. Alternatively, you can build and deploy this connector from source follow the below steps or use the more detailed tutorial in the athena-example module:

1. From the athena-federation-sdk dir, run `mvn clean install` if you haven't already.
2. From the athena-udfs dir, run `mvn clean install`.
3. From the athena-udfs dir, run  `../tools/publish.sh S3_BUCKET_NAME athena-udfs` to publish the connector to your private AWS Serverless Application Repository. The S3_BUCKET in the command is where a copy of the connector's code will be stored for Serverless Application Repository to retrieve it. This will allow users with permission to do so, the ability to deploy instances of the connector via 1-Click form. Then navigate to [Serverless Application Repository](https://aws.amazon.com/serverless/serverlessrepo)
4. Try using your UDF(s) in a query.

## License

This project is licensed under the Apache-2.0 License.
