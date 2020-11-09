[condition][]Image data has been changed on a node=property : ChangedPropertyFact ( name in ("j:node"), propertyName : name, propertyValue : nodeValue , node : node , $node : node, node.types contains "jnt:imageReference" || node.types contains "jmix:imageCaption")
[consequence][]Autogenerate image caption on the {node}=computerVisionService.describeImageRuleService({node}, drools);
