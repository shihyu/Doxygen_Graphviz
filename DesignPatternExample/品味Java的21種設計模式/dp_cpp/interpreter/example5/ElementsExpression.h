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
				namespace example5
				{

					using org::w3c::dom::Element;
					using org::w3c::dom::NodeList;
					///
					/// <summary> * ���Ԫ����Ϊ���ս���Ľ��ʹ������ </summary>
					/// 
					class ElementsExpression : public ReadXmlExpression
					{
					///	
					///	 <summary> * ������¼��ϵ�ReadXmlExpressionԪ�� </summary>
					///	 
					private:
						Collection<ReadXmlExpression*> *eles;
					///	
					///	 <summary> * Ԫ������ </summary>
					///	 
						std::string eleName;
					public:
						ElementsExpression(std::string eleName);

						virtual std::string *interpret(Context *c);

						virtual bool addEle(ReadXmlExpression *ele);
						virtual bool removeEle(ReadXmlExpression *ele);

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