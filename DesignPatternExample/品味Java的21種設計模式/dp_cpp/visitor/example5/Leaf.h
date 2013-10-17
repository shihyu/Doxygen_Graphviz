#pragma once

#include "Component.h"
#include "Visitor.h"
#include <string>

namespace cn
{
	namespace javass
	{
		namespace dp
		{
			namespace visitor
			{
				namespace example5
				{
					///
					/// <summary> * Ҷ�Ӷ����൱�ڷ�����ģʽ�ľ���Elementʵ�ֶ��� </summary>
					/// 
					class Leaf : public Component
					{
					public:
						virtual void accept(Visitor *visitor);
					///	
					///	 <summary> * Ҷ�Ӷ�������� </summary>
					///	 
					private:
						std::string name;
					///	
					///	 <summary> * ���췽��������Ҷ�Ӷ�������� </summary>
					///	 * <param name="name"> Ҷ�Ӷ�������� </param>
					///	 
					public:
						Leaf(std::string name);
						virtual std::string getName();

					private:
						bool initialized;
						void InitializeInstanceFields()
						{
							if ( ! initialized)
							{
								name = "";

								initialized = true;
							}
						}
					};

				}
			}
		}
	}
}