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
					///
					/// <summary> * Ԫ����Ϊ�ս����Ӧ�Ľ����� </summary>
					/// 
					class ElementTerminalExpression : public ReadXmlExpression
					{
					///	
					///	 <summary> * Ԫ�ص����� </summary>
					///	 
					private:
						std::string eleName;
					public:
						ElementTerminalExpression(std::string name);

						virtual std::string *interpret(Context *c);

					private:
						bool initialized;
						void InitializeInstanceFields()
						{
							if ( ! initialized)
							{
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