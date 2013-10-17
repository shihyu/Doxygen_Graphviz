#pragma once

#include "ReadXmlExpression.h"
#include "Context.h"
#include <string>

namespace cn
{
	namespace javass
	{
		namespace dp
		{
			namespace interpreter
			{
				namespace example3
				{

					using org::w3c::dom::Element;
					using org::w3c::dom::NodeList;
					///
					/// <summary> * Ԫ����Ϊ���ս����Ӧ�Ľ����������Ͳ�ִ���м�Ԫ�� </summary>
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