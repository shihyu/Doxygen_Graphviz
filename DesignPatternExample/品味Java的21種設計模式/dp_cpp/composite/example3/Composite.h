#pragma once

#include "Component.h"
#include <string>
#include <vector>

namespace cn
{
	namespace javass
	{
		namespace dp
		{
			namespace composite
			{
				namespace example3
				{

					///
					/// <summary> * ��϶��󣬿��԰���������϶������Ҷ�Ӷ��� </summary>
					/// 
					class Composite : public Component
					{
					///	
					///	 <summary> * �����洢��϶����а�������������� </summary>
					///	 
					private:
						std::vector<Component*> childComponents;
					///	
					///	 <summary> * ��϶�������� </summary>
					///	 
						std::string name;
					///	
					///	 <summary> * ���췽����������϶�������� </summary>
					///	 * <param name="name"> ��϶�������� </param>
					///	 
					public:
						Composite(std::string name);

						virtual void addChild(Component *child);
					///	
					///	 <summary> * �����϶�������Ľṹ </summary>
					///	 * <param name="preStr"> ǰ׺����Ҫ�ǰ��ղ㼶ƴ�ӵĿո�ʵ��������� </param>
					///	 
						virtual void printStruct(std::string preStr);

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