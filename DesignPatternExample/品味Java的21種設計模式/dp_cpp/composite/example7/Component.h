#pragma once

#include <string>

namespace cn
{
	namespace javass
	{
		namespace dp
		{
			namespace composite
			{
				namespace example7
				{

					///
					/// <summary> * ������������ </summary>
					/// 
					class Component
					{
					///	
					///	 <summary> * ��¼ÿ�������·�� </summary>
					///	 
					private:
						std::string componentPath;
					///	
					///	 <summary> * ��ȡ�����·�� </summary>
					///	 * <returns> �����·�� </returns>
					///	 
					public:
						virtual std::string getComponentPath();
					///	
					///	 <summary> * ���������·�� </summary>
					///	 * <param name="componentPath"> �����·�� </param>
					///	 
						virtual void setComponentPath(std::string componentPath);
					///	
					///	 <summary> * ��ȡ��������� </summary>
					///	 * <returns> ��������� </returns>
					///	 
						virtual std::string getName() = 0;

						/*-------------------������ԭ�еĶ���----------------------*/		

					///	
					///	 <summary> * ��������������� </summary>
					///	 
						virtual void printStruct(std::string preStr) = 0;

					///	
					///	 <summary> * ����϶����м����������  </summary>
					///	 * <param name="child"> ��������϶����е�������� </param>
					///	 
						virtual void addChild(Component *child);

					///	
					///	 <summary> * ����϶������Ƴ�ĳ��������� </summary>
					///	 * <param name="child"> ���Ƴ���������� </param>
					///	 
						virtual void removeChild(Component *child);

					///	
					///	 <summary> * ����ĳ��������Ӧ��������� </summary>
					///	 * <param name="index"> ��Ҫ��ȡ����������������������0��ʼ </param>
					///	 * <returns> ������Ӧ��������� </returns>
					///	 
						virtual Component *getChildren(int index);

					private:
						bool initialized;
						void InitializeInstanceFields()
						{
							if ( ! initialized)
							{
								componentPath = "";

								initialized = true;
							}
						}

public:
	Component()
	{
		InitializeInstanceFields();
	}
					};


				}
			}
		}
	}
}