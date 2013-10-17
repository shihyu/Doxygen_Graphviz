#pragma once

#include "Component.h"
#include "Visitor.h"
#include <string>
#include <vector>

namespace cn
{
	namespace javass
	{
		namespace dp
		{
			namespace visitor
			{
				namespace example6
				{
					///
					/// <summary> * ��϶��󣬿��԰���������϶������Ҷ�Ӷ���
					/// * �൱�ڷ�����ģʽ�ľ���Elementʵ�ֶ��� </summary>
					/// 
					class Composite : public Component
					{
					public:
						virtual void accept(Visitor *visitor);
					///	
					///	 <summary> * �����洢��϶����а�������������� </summary>
					///	 
					private:
						std::vector<Component*> childComponents;
					public:
						virtual std::vector<Component*> getChildComponents();
					///	
					///	 <summary> * ��϶�������� </summary>
					///	 
					private:
						std::string name;
					///	
					///	 <summary> * ���췽����������϶�������� </summary>
					///	 * <param name="name"> ��϶�������� </param>
					///	 
					public:
						Composite(std::string name);

						virtual void addChild(Component *child);
						virtual std::string getName();

					private:
						bool initialized;
						void InitializeInstanceFields()
						{
							if ( ! initialized)
							{
								childComponents = std::vector<Component*>();
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