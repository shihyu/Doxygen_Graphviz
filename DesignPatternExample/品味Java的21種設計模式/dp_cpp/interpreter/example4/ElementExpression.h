#pragma once

#include "ReadXmlExpression.h"
#include "Context.h"
#include <string>
#include <vector>

namespace cn
{
	namespace javass
	{
		namespace dp
		{
			namespace interpreter
			{
				namespace example4
				{

					using org::w3c::dom::Element;
					using org::w3c::dom::NodeList;

					///
					/// <summary> * ����Ԫ����Ϊ���ս���Ľ����� </summary>
					/// 
					class ElementExpression : public ReadXmlExpression
					{
					///	
					///	 <summary> * ������¼��ϵ�ReadXmlExpressionԪ�� </summary>
					///	 
					private:
						Collection<ReadXmlExpression*> *eles;
					///	
					///	 <summary> * Ԫ�ص����� </summary>
					///	 
						std::string eleName;
					public:
						ElementExpression(std::string eleName);
						virtual bool addEle(ReadXmlExpression *ele);
						virtual bool removeEle(ReadXmlExpression *ele);

						virtual std::string *interpret(Context *c);

					private:
						bool initialized;
						void InitializeInstanceFields()
						{
							if ( ! initialized)
							{
								eles = std::vector<ReadXmlExpression*>();
								eleName = "";

								initialized = true;
							}
						}
					};
				}
			}
		}
	}
}